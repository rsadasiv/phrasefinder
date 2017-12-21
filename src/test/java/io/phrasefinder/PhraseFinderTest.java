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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

class PhraseFinderTest {
  
  private static final String ENG_US_QUERY = "I struggled ???";
  private static final String ENG_US_EXPECTED_PHRASE_0 = "I struggled to my feet";
  private static final String ENG_US_EXPECTED_PHRASE_1 = "I struggled to keep my";
  private static final String ENG_US_EXPECTED_PHRASE_2 = "I struggled to sit up";

  @Test
  void testSearchCorpusString() throws IOException {
    Result result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, ENG_US_QUERY);
    assertEquals(100, result.getPhrases().length);
    assertEquals(ENG_US_EXPECTED_PHRASE_0, result.getPhrases()[0].toString());
    assertEquals(ENG_US_EXPECTED_PHRASE_1, result.getPhrases()[1].toString());
    assertEquals(ENG_US_EXPECTED_PHRASE_2, result.getPhrases()[2].toString());
  }

  @Test
  void testSearchCorpusStringOptions() throws IOException {
    Options options = new Options();
    options.setMaxResults(3);
    Result result = PhraseFinder.search(Corpus.AMERICAN_ENGLISH, ENG_US_QUERY, options);
    assertEquals(3, result.getPhrases().length);
    assertEquals(ENG_US_EXPECTED_PHRASE_0, result.getPhrases()[0].toString());
    assertEquals(ENG_US_EXPECTED_PHRASE_1, result.getPhrases()[1].toString());
    assertEquals(ENG_US_EXPECTED_PHRASE_2, result.getPhrases()[2].toString());
  }

}
