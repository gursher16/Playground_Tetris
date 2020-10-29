### Playground_Tetris

A simple, console based tetris game.  
Inspired by javidx9 (https://www.youtube.com/channel/UC-yuWVUplUJZvieEligKBkA) a.k.a OneLoneCoder a.k.a The Bob Ross of programming. The main motive of this small project was to practice arrays. 

To compile, simply navigate to the *src/com/playground/tetris* folder using the command line interface and enter the following command:
```
javac *.java
```
To run the program, enter the following command:
(replace *folderLocation* with the full path to the src/com/playground/tetris folder )
```
java -cp [folderLocaton] com.playground.tetris.Tetris
```

### Controls
&larr; = move piece left  
&rarr; = move piece right  
&darr; = move piece down  
&uarr; = rotate piece  

### Note
In order to bypass Java console IO limitations, an empty JFrame is used to capture user input

