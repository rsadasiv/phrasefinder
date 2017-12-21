// This file is part of PhraseFinder. http://phrasefinder.io
//
// Copyright (C) 2016-2017 Martin Trenkmann
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package io.phrasefinder;

/**
 * Corpus is an enum type that represents a corpus to be searched. All corpora belong to version 2
 * of the <a href="http://storage.googleapis.com/books/ngrams/books/datasetsv2.html">Google Books
 * Ngram Dataset</a>.
 */
public enum Corpus {

  AMERICAN_ENGLISH, BRITISH_ENGLISH, CHINESE, FRENCH, GERMAN, RUSSIAN, SPANISH;

  public static Corpus fromOrdinal(int ordinal) {
    if (ordinal >= 0 && ordinal < values().length) {
      return values()[ordinal];
    }
    throw new IllegalArgumentException("Invalid ordinal");
  }

  public String shortName() {
    switch (this) {
      case AMERICAN_ENGLISH:
        return "eng-us";
      case BRITISH_ENGLISH:
        return "eng-gb";
      case CHINESE:
        return "chi";
      case FRENCH:
        return "fre";
      case GERMAN:
        return "ger";
      case RUSSIAN:
        return "rus";
      case SPANISH:
        return "spa";
      default:
        throw new Error();
    }
  }

}
