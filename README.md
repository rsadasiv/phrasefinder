# PhraseFinder Java Client

[PhraseFinder](http://phrasefinder.io) is a search engine for the [Google Books Ngram Dataset](http://storage.googleapis.com/books/ngrams/books/datasetsv2.html) (version 2). This repository contains the official Java client for requesting PhraseFinder's web [API](http://phrasefinder.io/api) which is free to use for any purpose.

* [Documentation](http://phrasefinder.io/documentation)
* [Java API Reference](https://mtrenkmann.github.io/phrasefinder-client-java/)

## Installation with Maven

The PhraseFinder client library for Java is build using Maven, which can be installed from your operating system's package manager or downloaded from <http://maven.apache.org/>. If you would rather build without Maven, see below.

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

You can either copy the single source file `src/main/java/io/phrasefinder/Phrasefinder.java` into your own Java project, or download a pre-build .jar file from the release page of this repository.


## Example

```java
import java.io.IOException;

import io.phrasefinder.PhraseFinder;
import io.phrasefinder.PhraseFinder.Params;
import io.phrasefinder.PhraseFinder.Phrase;
import io.phrasefinder.PhraseFinder.Result;
import io.phrasefinder.PhraseFinder.Status;
import io.phrasefinder.PhraseFinder.Token;

public final class Example {

  public static void main(String[] args) {

    // Set up your query.
    String query = "I struggled ???";

    // Optional: set the maximum number of phrases to return.
    Params params = new Params();
    params.setMaxResults(10);

    // Send the request.
    Result result;
    try {
      result = PhraseFinder.search(query, params);
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
javac Example.java src/main/java/io/phrasefinder/PhraseFinder.java
java -cp src/main/java:. Example

// Delete class files
rm src/main/java/io/phrasefinder/PhraseFinder*.class Example.class
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
