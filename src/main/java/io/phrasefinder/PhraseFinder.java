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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * The PhraseFinder class provides (static) routines for querying the
 * <a href="http://phrasefinder.io">PhraseFinder</a> web service.
 * 
 * @see PhraseFinder#search(String)
 * @see PhraseFinder#search(String, Params)
 */
public final class PhraseFinder {
  
  /**
   * The url to send search requests to.
   */
  public static final String BASE_URL = "http://phrasefinder.io/search";

  /**
   * Corpus is an enum type that represents a corpus to be searched. All corpora belong to version 2
   * of the <a href="http://storage.googleapis.com/books/ngrams/books/datasetsv2.html">Google Books
   * Ngram Dataset</a>.
   */
  public static enum Corpus {
    AMERICAN_ENGLISH, BRITISH_ENGLISH, CHINESE, FRENCH, GERMAN, RUSSIAN, SPANISH
  }

  /**
   * Status is an enum type that reports whether a request was successful or not. The value is
   * derived from the HTTP status code sent along with a response. Note that
   * {@link Status#ordinal()} does not correspond to the original HTTP code.
   */
  public static enum Status {
    /**
     * The request was successful.
     */
    OK,

    /**
     * A required parameter was missing or a parameter had an invalid value.
     */
    BAD_REQUEST,

    /**
     * The service is currently down. Please try again later.
     */
    BAD_GATEWAY
  }

  /**
   * Token represents a single token (word, punctuation mark, etc.) as part of a phrase.
   */
  public static class Token {

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
      COMPLETED
    }

    private Tag tag;
    private String text;

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

  /**
   * Phrase represents an n-gram from the dataset. A phrase consists of a sequence of tokens and
   * metadata.
   */
  public static class Phrase {

    private Token[] tokens;
    private long matchCount;
    private int volumeCount;
    private int firstYear;
    private int lastYear;
    private int relativeId;
    private double score;

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
     * Returns the phrase's relative id.
     */
    public int getRelativeId() {
      return relativeId;
    }

    /**
     * Returns the phrase's score, also called its relative frequency.
     */
    public double getScore() {
      return score;
    }
  }

  /**
   * Params represents parameters that can be sent along with a query.
   */
  public static class Params {

    private static final Params DEFAULT_INSTANCE = new Params();

    private Corpus corpus = Corpus.AMERICAN_ENGLISH;
    private int minPhraseLength = 1;
    private int maxPhraseLength = 5;
    private int maxResults = 100;

    /**
     * Returns the type of the corpus to be searched.
     */
    public Corpus getCorpus() {
      return corpus;
    }

    /**
     * Sets the corpus to be searched. Defaults to {@link Corpus#AMERICAN_ENGLISH} if not set.
     */
    public void setCorpus(Corpus corpus) {
      this.corpus = corpus;
    }

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

  /**
   * Result represents a search result.
   */
  public static class Result {

    private Status status;
    private Phrase[] phrases;

    /**
     * Returns the status of the response.
     */
    public Status getStatus() {
      return status;
    }

    /**
     * Returns the matching phrases.
     */
    public Phrase[] getPhrases() {
      return phrases;
    }
  }

  private PhraseFinder() {}

  /**
   * Sends a search request with default parameters.
   * 
   * @param query is the query string.
   * @return A {@link Result} object that contains matching phrases if {@link Result#getStatus()}
   *         yields {@link Status#OK}. Any other status value is considered an unsuccessful request.
   * @throws IOException when sending the request or receiving the response failed.
   */
  public static Result search(String query) throws IOException {
    return search(query, Params.DEFAULT_INSTANCE);
  }

  /**
   * Sends a search request with custom parameters.
   * 
   * @param query is the query string.
   * @param params are additional request parameters.
   * @return Same as {@link #search(String)}
   * @throws IOException Same as {@link #search(String)}
   */
  public static Result search(String query, Params params) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) toUrl(query, params).openConnection();
    Result result = new Result();
    result.status = toStatus(connection.getResponseCode());
    if (result.status == Status.OK) {
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream(),
              java.nio.charset.StandardCharsets.UTF_8))) {
        String line = null;
        List<Phrase> phrases = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
          String[] fields = line.split("\t");
          String[] tokens = fields[0].split(" ");
          Phrase phrase = new Phrase();
          phrase.tokens = new Token[tokens.length];
          for (int i = 0; i < tokens.length; i++) {
            int tokenLength = tokens[i].length();
            phrase.tokens[i] = new Token();
            phrase.tokens[i].tag = toTag(Integer.parseInt(tokens[i].substring(tokenLength - 1)));
            phrase.tokens[i].text = tokens[i].substring(0, tokenLength - 2);
          }
          phrase.matchCount = Long.parseLong(fields[1]);
          phrase.volumeCount = Integer.parseInt(fields[2]);
          phrase.firstYear = Integer.parseInt(fields[3]);
          phrase.lastYear = Integer.parseInt(fields[4]);
          phrase.relativeId = Integer.parseInt(fields[5]);
          phrase.score = Double.parseDouble(fields[6]);
          phrases.add(phrase);
        }
        result.phrases = phrases.toArray(new Phrase[0]);
      }
    }
    return result;
  }

  private static Status toStatus(int httpResponseCode) {
    switch (httpResponseCode) {
      case 200:
        return Status.OK;
      case 400:
        return Status.BAD_REQUEST;
      case 502:
        return Status.BAD_GATEWAY;
      default:
        throw new IllegalArgumentException();
    }
  }

  private static String toString(Corpus corpus) {
    switch (corpus) {
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
        throw new IllegalArgumentException();
    }
  }

  private static Token.Tag toTag(int value) {
    if (value < Token.Tag.values().length) {
      return Token.Tag.values()[value];
    }
    throw new IllegalArgumentException();
  }

  private static URL toUrl(String query, Params options)
      throws UnsupportedEncodingException, MalformedURLException {
    StringBuilder sb = new StringBuilder();
    sb.append(BASE_URL).append("?format=tsv");
    sb.append("&query=")
        .append(URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8.toString()));
    sb.append("&corpus=").append(toString(options.getCorpus()));
    sb.append("&nmin=").append(options.getMinPhraseLength());
    sb.append("&nmax=").append(options.getMaxPhraseLength());
    sb.append("&topk=").append(options.getMaxResults());
    return new URL(sb.toString());
  }
}
