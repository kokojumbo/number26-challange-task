# number26-challange-task
Task for number26. 
Simple Rest service based on Jersey. 
KISS :)

## Test
    mvn clean test

## Run
    mvn exec:java

## Performence comment
I optimized my code in respect of performence. 
By using hashmaps solutions I can get performance like this:
- GET transaction - O(1)
- GET sum of transactions - O(1)
- GET ids of type - O(1)
- PUT transaction - O(p) - where p is the longest path in the transactions graph

(Of course we can switch this complexity and move the logic for the GET side, but I 	decided that is better to get faster reading than loading)
    
The disadventage of the solution is that I have to maintain transactions data in three hashmaps instead of one, but efficiency is better.