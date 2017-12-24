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

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PhraseTest {

  @Test
  void hasNoPublicConstructors() {
    assertTrue(Phrase.class.getConstructors().length == 0);
  }

  @Test
  void emptyPhraseIsZeroInitialized() {
    assertTrue(Phrase.getEmptyPhrase().tokens.length == 0);
    assertTrue(Phrase.getEmptyPhrase().getMatchCount() == 0);
    assertTrue(Phrase.getEmptyPhrase().getVolumeCount() == 0);
    assertTrue(Phrase.getEmptyPhrase().getFirstYear() == 0);
    assertTrue(Phrase.getEmptyPhrase().getLastYear() == 0);
    assertTrue(Phrase.getEmptyPhrase().getScore() == 0);
    assertTrue(Phrase.getEmptyPhrase().getId() == 0);
  }

}
