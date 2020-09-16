-- Laba po paralelnomu #1

{-# LANGUAGE ScopedTypeVariables #-}
module Main where

import           Control.Parallel.Strategies
import           System.Environment
import           System.IO
import           System.Random
import           Data.Time
import           Control.DeepSeq

-- Split list into chunks of n length each
chunkifyList :: Int -> [a] -> [[a]]
chunkifyList _ []   = []
chunkifyList n list = chunk : chunkifyList n rest
  where (chunk, rest) = splitAt n list

-- Generate list of random integers
randArray :: Int -> IO [Int]
randArray n = getStdGen >>= return . take n . randomRs (0, n)

-- Non-parallel vector sum
sumVecsSeq :: (Num a) => [a] -> [a] -> [a]
sumVecsSeq = zipWith (+)

sumVecsPar :: (Num a) => [a] -> [a] -> [a]
sumVecsPar a b = concat (processList `using` chunkParallelism)
 where
  zipped    = (zip a b) -- Create list of pairs
  chunksize = length zipped `div` 12000
  chunked   = chunkifyList chunksize zipped -- Split list into chunks
  sumTuple (i, j) = i + j
  processList      = map (map sumTuple) chunked
  chunkParallelism = parList rseq

main :: IO ()
main = do
  hSetBuffering stdout NoBuffering
  [n, usePar] <- getArgs
  let arrLen :: Int = read n

  arr1 <- randArray arrLen
  arr2 <- randArray arrLen
  let sequentialSum = sumVecsSeq arr1 arr2
  let parallelSum   = sumVecsPar arr1 arr2

  if usePar == "par"
    then deepseq parallelSum $ return ()
    else deepseq sequentialSum $ return ()
