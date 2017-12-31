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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PhraseTest {

  @Test
  void hasNoPublicConstructors() {
    assertTrue(Phrase.class.getConstructors().length == 0);
  }

  @Test
  void emptyPhraseIsZeroInitialized() {
    assertEquals(Corpus.NULL, Phrase.getEmptyPhrase().getCorpus());
    assertEquals(0, Phrase.getEmptyPhrase().tokens.length);
    assertEquals(0, Phrase.getEmptyPhrase().getMatchCount());
    assertEquals(0, Phrase.getEmptyPhrase().getVolumeCount());
    assertEquals(0, Phrase.getEmptyPhrase().getFirstYear());
    assertEquals(0, Phrase.getEmptyPhrase().getLastYear());
    assertEquals(0, Phrase.getEmptyPhrase().getScore());
    assertEquals(0, Phrase.getEmptyPhrase().getId());
  }

}
