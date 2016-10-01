# PhraseFinder Java Client

The official Java client for the [PhraseFinder](http://phrasefinder.io) web service

* [Documentation](https://mtrenkmann.github.io/phrasefinder-client-java/)

## Demo

```java
import io.phrasefinder.PhraseFinder;
import io.phrasefinder.PhraseFinder.Options;
import io.phrasefinder.PhraseFinder.Phrase;
import io.phrasefinder.PhraseFinder.Result;
import io.phrasefinder.PhraseFinder.Status;
import io.phrasefinder.PhraseFinder.Token;

import java.io.IOException;

public final class Demo {

  public static void main(String[] args) {
    
    // Set up your query.
    String query = "I like ???";
    
    // Set the optional parameter topk to 10.
    Options options = new Options();
    options.topk = 10;
    
    // Perform a request.
    Result result;
    try {
      result = PhraseFinder.search(query, options);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    if (result.status != Status.OK) {
      System.out.println("Request was not successful: " + result.status);
      return;
    }
    
    // Print phrases line by line.
    for (Phrase phrase : result.phrases) {
      System.out.printf("%6f", phrase.score);
      for (Token token : phrase.tokens) {
        System.out.printf(" %s_%d", token.text, token.tag.ordinal());
      }
      System.out.println();
    }
    System.out.println("Remaining quota: " + result.quota);
  }
}
```

## Clone, compile, run

```sh
git clone https://github.com/mtrenkmann/phrasefinder-client-java.git
cd phrasefinder-client-java
javac -sourcepath src src/Demo.java
java -classpath src Demo
```

## Output

```
0.175468 I_0 like_0 to_1 think_1 of_1
0.165350 I_0 like_0 to_1 think_1 that_1
0.149246 I_0 like_0 it_1 ._1 "_1
0.104326 I_0 like_0 it_1 ,_1 "_1
0.091746 I_0 like_0 the_1 way_1 you_1
0.082627 I_0 like_0 the_1 idea_1 of_1
0.064459 I_0 like_0 that_1 ._1 "_1
0.057900 I_0 like_0 it_1 very_1 much_1
0.055201 I_0 like_0 you_1 ._1 "_1
0.053677 I_0 like_0 the_1 sound_1 of_1
Remaining quota: 99
```

## Installation

Copy the content of the `src` folder into the source directory of your project.
