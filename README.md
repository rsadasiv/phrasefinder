# PhraseFinder Java Client

[PhraseFinder](http://phrasefinder.io) is a search engine for the [Google Books Ngram Dataset](http://storage.googleapis.com/books/ngrams/books/datasetsv2.html) (version 2). This repository contains the official Java client for requesting PhraseFinder's web [API](http://phrasefinder.io/api) which is free to use for any purpose.

* [Documentation](http://phrasefinder.io/documentation)
* [Java API Reference](https://mtrenkmann.github.io/phrasefinder-client-java/)

## Installation with Maven

The PhraseFinder client library for Java is build using Maven, which can be installed from your operating system's package manager or downloaded from <http://maven.apache.org/>.

To build and install the library into your local Maven repository, run

```sh
mvn install
```

Then, add the following dependency to the `pom.xml` of your own Maven managed project:

```xml
<dependency>
  <groupId>io.phrasefinder</groupId>
  <artifactId>phrasefinder</artifactId>
  <version>0.2.0</version>
</dependency>
```

Alternatively, if you want to build a .jar file without installing it, run

```sh
mvn package
```

The .jar file will be placed in the `target` directory.

## Installation without Maven

Download the latest .jar file from the release page of this repository and add it to your
project's classpath.


## Example

```java
import java.io.IOException;

import io.phrasefinder.Corpus;
import io.phrasefinder.Options;
import io.phrasefinder.Phrase;
import io.phrasefinder.Phrase.Token;
import io.phrasefinder.PhraseFinder;
import io.phrasefinder.Result;
import io.phrasefinder.Result.Status;

public final class Example {

  public static void main(String[] args) {

    // Set up your query.
    String query = "I struggled ???";

    // Optional: set the maximum number of phrases to return.
    Options options = new Options();
    options.setMaxResults(10);

    // Send the request.
    Result result;
    try {
      result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, query, options);
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }

    if (result.getStatus() != Status.OK) {
      System.out.println("Request was not successful: " + result.getStatus());
      return;
    }

    // Print phrases line by line.
    for (Phrase phrase : result.getPhrases()) {
      System.out.printf("%6f", phrase.getScore());
      for (Token token : phrase.getTokens()) {
        System.out.printf(" %s", token.getText());
      }
      System.out.println();
    }
  }
}
```

### Compile and Run

```sh
git clone https://github.com/mtrenkmann/phrasefinder-client-java.git
cd phrasefinder-client-java
javac src/main/java/io/phrasefinder/*.java src/main/java/io/phrasefinder/examples/Example.java
java -cp src/main/java io.phrasefinder.examples.Example

# Delete class files.
rm src/main/java/io/phrasefinder/*.class src/main/java/io/phrasefinder/examples/Example.class
```

### Output

```
0.358441 I struggled to my feet
0.138214 I struggled to keep my
0.087247 I struggled to sit up
0.075033 I struggled to free myself
0.065726 I struggled to keep up
0.062164 I struggled to find the
0.056420 I struggled to get my
0.055257 I struggled to get up
0.051694 I struggled to get away
0.049804 I struggled to find a
```
