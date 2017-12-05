// This file is part of PhraseFinder.  http://phrasefinder.io
//
// Copyright (C) 2016  Martin Trenkmann
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
        System.out.printf(" %s_%d", token.getText(), token.getTag().ordinal());
      }
      System.out.println();
    }
  }
}
