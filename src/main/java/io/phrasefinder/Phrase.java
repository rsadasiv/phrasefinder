// This file is part of PhraseFinder. http://phrasefinder.io
//
// Copyright (C) 2016-2018 Martin Trenkmann
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
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Phrase represents an n-gram from the dataset. For this reason, objects of this class cannot be
 * constructed by the user (there are no public constructors) and objects returned from a search
 * request are immutable (there are no public setter methods). Client code that needs mutable phrase
 * objects is required to introduce their own Phrase class.
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
       * The token has been inserted either by an application of the <b>?</b> or the <b>*</b>
       * operator.
       */
      INSERTED,

      /**
       * The token was given as the left- or right-hand side of the <b>/</b> operator.
       */
      ALTERNATIVE,

      /**
       * The token has been completed by an application of the <b>+</b> operator.
       */
      COMPLETED;

      /**
       * Returns the enum constant of this type with the specified ordinal number.
       * 
       * @param ordinal the ordinal number of the enum constant to be returned.
       * @return the enum constant with the specified ordinal number
       */
      public static Tag fromOrdinal(int ordinal) {
        if (ordinal >= 0 && ordinal < values().length) {
          return values()[ordinal];
        }
        throw new IllegalArgumentException("Invalid ordinal number");
      }
    }

    private final Tag tag;
    private final String text;

    protected Token(Tag tag, String text) {
      this.tag = Objects.requireNonNull(tag);
      this.text = Objects.requireNonNull(text);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + tag.hashCode();
      result = prime * result + text.hashCode();
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
       * @return 
     */
    public Tag getTag() {
      return tag;
    }

    /**
     * Returns the token's text.
       * @return 
     */
    public String getText() {
      return text;
    }
  }

  public Token[] tokens;
  protected long matchCount;
  protected int volumeCount;
  protected int firstYear;
  protected int lastYear;
  protected double score;
  protected long id;

  private static final Token[] EMPTY_TOKENS = new Token[0];

  protected Phrase() {
    this.tokens = EMPTY_TOKENS;
  }

  private static final Phrase EMPTY_INSTANCE = new Phrase();

  /**
   * Returns an empty phrase with a zero-size token array and all metadata set to zero. The empty
   * phrase can be used instead of {@code null} to represent the absence of a phrase and/or to
   * reduce null checks in client code.
     * @return 
   */
  public static Phrase emptyInstance() {
    return EMPTY_INSTANCE;
  }

  @Override
  public int hashCode() {
    return (int) (id ^ (id >>> 32));
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Phrase))
      return false;
    Phrase other = (Phrase) obj;
    // Since instances of this class are immutable and can only be created by this library and not
    // by the user, it is guaranteed that the id of a phrase sent by the server is always unique.
    // Hence, it is not necessary to compare the actual tokens.
    return id != other.id;
  }

  /**
   * Returns a whitespace-delimited concatenation of the token's text.
   * 
     * @return 
   * @see Token#getText()
   */
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

  /**
   * Returns the corpus enum constant the phrase belongs to.
   */
  public Corpus getCorpus() {
    return Corpus.fromOrdinal((int) (id >>> 40) & 0xFFFF);
  }

}
