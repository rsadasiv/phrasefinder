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

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Phrase represents an n-gram from the dataset. A phrase consists of a sequence of tokens and
 * metadata.
 */
public class Phrase {

  /**
   * Token represents a single token (word, punctuation mark, etc.) as part of a phrase.
   */
  public static final class Token {

    /**
     * Tag is an enum type that denotes the role of a token with respect to the query.
     */
    public static enum Tag {
      /**
       * The token was given as part of the query string.
       */
      GIVEN,

      /**
       * The token has been inserted either by an application of the "?" or the "*" operator.
       */
      INSERTED,

      /**
       * The token was given as the left- or right-hand side of the "/" operator.
       */
      ALTERNATIVE,

      /**
       * The token has been completed by an application of the "+" operator.
       */
      COMPLETED;
      
      public static Tag fromOrdinal(int ordinal) {
        if (ordinal >= 0 && ordinal < values().length) {
          return values()[ordinal];
        }
        throw new IllegalArgumentException("Invalid ordinal");
      }
    }

    protected Tag tag;
    protected String text;
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((tag == null) ? 0 : tag.hashCode());
      result = prime * result + ((text == null) ? 0 : text.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Token other = (Token) obj;
      if (tag != other.tag)
        return false;
      if (text == null) {
        if (other.text != null)
          return false;
      } else if (!text.equals(other.text))
        return false;
      return true;
    }

    /**
     * Returns the token's tag.
     */
    public Tag getTag() {
      return tag;
    }

    /**
     * Returns the token's text.
     */
    public String getText() {
      return text;
    }
  }

  protected Token[] tokens;
  protected long matchCount;
  protected int volumeCount;
  protected int firstYear;
  protected int lastYear;
  protected double score;
  protected long id;
  
  
  
  @Override
  public String toString() {
    return Arrays.stream(tokens).map(t -> t.text).collect(Collectors.joining(" "));
  }
  
  /**
   * Returns the phrase's tokens.
   */
  public Token[] getTokens() {
    return tokens;
  }

  /**
   * Returns the phrase's match count, also called its absolute frequency.
   */
  public long getMatchCount() {
    return matchCount;
  }

  /**
   * Returns the phrase's volume count, which is the number of books the phrase appears in.
   */
  public int getVolumeCount() {
    return volumeCount;
  }

  /**
   * Returns the phrase's first year of occurrence.
   */
  public int getFirstYear() {
    return firstYear;
  }

  /**
   * Returns the phrase's last year of occurrence.
   */
  public int getLastYear() {
    return lastYear;
  }

  /**
   * Returns the phrase's score, also called its relative frequency.
   */
  public double getScore() {
    return score;
  }

  /**
   * Returns the phrase's id which is unique among all corpora.
   */
  public long getId() {
    return id;
  }

}
