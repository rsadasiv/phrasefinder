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
 * @see PhraseFinder#search(String, Options)
 */
public final class PhraseFinder {

  /**
   * Corpus is an enum type that represents a corpus to be searched. All corpora belong to version
   * 2 of the <a href="http://storage.googleapis.com/books/ngrams/books/datasetsv2.html">Google
   * Books Ngram Dataset</a>.
   */
  public static enum Corpus {
    AMERICAN_ENGLISH, BRITISH_ENGLISH, CHINESE, FRENCH, GERMAN, RUSSIAN, SPANISH
  }

  /**
   * Status is a type that reports whether a request was successful or not. The value is derived
   * from the HTTP status code sent along with a response. Note that {@link Status#ordinal()} does
   * not correspond to the original HTTP code.
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
     * Tag denotes the role of a token with respect to the query.
     */
    public static enum Tag {
      /**
       * The token was given as part of the query.
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

    public Token(Tag tag, String text) {
      this.tag = tag;
      this.text = text;
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

  /**
   * Phrase represents a phrase, also called n-gram. A phrase consists of a sequence of tokens and
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
     * Returns the phrase's volume count which is the number of books the phrase appears in.
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
   * Options represents optional parameters that can be sent along with a query.
   */
  public static class Options {

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
   * Sends a request with default parameters.
   * 
   * @param query is the query string.
   * @return A {@link Result} object whose {@link Result#status} field is equal to {@link Status#OK}
   *         if the request was successful. In this case other fields of the object are in valid
   *         state and can be read. Any status other than {@link Status#OK} indicates a failed
   *         request. In that case other fields in the result have unspecified data.
   * @throws IOException when sending the request or receiving the response failed.
   */
  public static Result search(String query) throws IOException {
    return search(query, new Options());
  }

  /**
   * Sends a request to the server with parameters.
   * 
   * @param query is the query string.
   * @param options are the parameters.
   * @return Same as {@link #search(String)}
   * @throws IOException Same as {@link #search(String)}
   */
  public static Result search(String query, Options options) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) toUrl(query, options).openConnection();
    Result response = new Result();
    response.status = toStatus(connection.getResponseCode());
    if (response.status == Status.OK) {
      try (BufferedReader reader =
          new BufferedReader(new InputStreamReader(connection.getInputStream(),
              java.nio.charset.StandardCharsets.UTF_8))) {
        String line = null;
        List<Phrase> phrases = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split("\t");
          String[] terms = parts[0].split(" ");
          Token[] tokens = new Token[terms.length];
          for (int i = 0; i < terms.length; i++) {
            int termLength = terms[i].length();
            tokens[i] = new Token(toTag(Integer.parseInt(terms[i].substring(termLength - 1))),
                terms[i].substring(0, termLength - 2));
          }
          Phrase phrase = new Phrase();
          phrase.tokens = tokens;
          phrase.matchCount = Long.parseLong(parts[1]);
          phrase.volumeCount = Integer.parseInt(parts[2]);
          phrase.firstYear = Integer.parseInt(parts[3]);
          phrase.lastYear = Integer.parseInt(parts[4]);
          phrase.relativeId = Integer.parseInt(parts[5]);
          phrase.score = Double.parseDouble(parts[6]);
          phrases.add(phrase);
        }
        response.phrases = phrases.toArray(new Phrase[0]);
      }
    }
    return response;
  }

  private static Status toStatus(int httpResponseCode) {
    switch (httpResponseCode) {
      case 200:
        return Status.OK;
      case 400:
        return Status.BAD_REQUEST;
      case 405:
        return Status.METHOD_NOT_ALLOWED;
      case 500:
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

  private static URL toUrl(String query, Options options)
      throws UnsupportedEncodingException, MalformedURLException {
    StringBuilder sb = new StringBuilder();
    sb.append("http://phrasefinder.io/search?format=tsv");
    sb.append("&query=")
        .append(URLEncoder.encode(query, java.nio.charset.StandardCharsets.UTF_8.toString()));
    sb.append("&corpus=").append(toString(options.getCorpus()));
    sb.append("&nmin=").append(options.getMinPhraseLength());
    sb.append("&nmax=").append(options.getMaxPhraseLength());
    sb.append("&topk=").append(options.getMaxResults());
    return new URL(sb.toString());
  }
}
