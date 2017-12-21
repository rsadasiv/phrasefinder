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
 * Options represents optional parameters that can be sent along with a query.
 */
public class Options {

  private int minPhraseLength = 1;
  private int maxPhraseLength = 5;
  private int maxResults = 100;

  /**
   * Returns the minimum length of matching phrases (number of tokens) to be included in the
   * result set.
   */
  public int getMinPhraseLength() {
    return minPhraseLength;
  }

  /**
   * Sets the minimum length of matching phrases (number of tokens) to be included in the result
   * set. Defaults to 1 if not set.
   */
  public void setMinPhraseLength(int minPhraseLength) {
    this.minPhraseLength = minPhraseLength;
  }

  /**
   * Returns the maximum length of matching phrases (number of tokens) to be included in the
   * result set.
   */
  public int getMaxPhraseLength() {
    return maxPhraseLength;
  }

  /**
   * Sets the maximum length of matching phrases (number of tokens) to be included in the result
   * set. Defaults to 5 if not set.
   */
  public void setMaxPhraseLength(int maxPhraseLength) {
    this.maxPhraseLength = maxPhraseLength;
  }

  /**
   * Returns the maximum number of phrases to be returned.
   */
  public int getMaxResults() {
    return maxResults;
  }

  /**
   * Sets the maximum number of phrases to be returned. A smaller value may lead to slightly
   * faster response times. Defaults to 100 if not set.
   */
  public void setMaxResults(int maxResults) {
    this.maxResults = maxResults;
  }

}
