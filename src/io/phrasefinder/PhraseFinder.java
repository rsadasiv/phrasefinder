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

public final class PhraseFinder {
  
  public static enum Corpus {
    ENGLISH_US,  // 0
    ENGLISH_GB,  // 1
    SPANISH,     // 2
    FRENCH,      // 3
    GERMAN,      // 4
    RUSSIAN,     // 5
    CHINESE      // 6
  }
  
  public static enum Status {
    OK,                  // 0
    BAD_REQUEST,         // 1
    PAYMENT_REQUIRED,    // 2
    METHOD_NOT_ALLOWED,  // 3
    TOO_MANY_REQUESTS,   // 4
    SERVER_ERROR         // 5
  }
  
  public static class Token {
    public static enum Tag {
      GIVEN,        // 0
      INSERTED,     // 1
      ALTERNATIVE,  // 2
      COMPLETED     // 3
    }
    public String text;
    public Tag    tag;
  }
  
  public static class Phrase {
    public Token[] tokens;
    public long    matchCount;
    public int     volumeCount;
    public int     firstYear;
    public int     lastYear;
    public int     relativeId;
    public double  score;
  }
  
  public static class Options {
    public Corpus corpus = Corpus.ENGLISH_US;
    public String key    = null;
    public int    nmin   = 1;
    public int    nmax   = 5;
    public int    topk   = 100;
  }
  
  public static class Result {
    public Status   status;
    public Phrase[] phrases;
    public int      quota;
  }
  
  public static Result search(String query) throws IOException {
    return search(query, new Options());
  }
  
  public static Result search(String query, Options options) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) toUrl(query, options).openConnection();
    Result response = new Result();
    response.status = toStatus(connection.getResponseCode());
    if (response.status == Status.OK) {
      response.quota = Integer.parseInt(connection.getHeaderField("X-Quota"));
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(connection.getInputStream(),
              java.nio.charset.StandardCharsets.UTF_8))) {
        String line = null;
        List<Phrase> phrases = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
          Phrase phrase = new Phrase();
          String[] parts = line.split("\t");
          String[] tokens = parts[0].split(" ");
          phrase.tokens = new Token[tokens.length];
          for (int i = 0; i != tokens.length; ++i) {
            int length = tokens[i].length();
            phrase.tokens[i]      = new Token();
            phrase.tokens[i].text = tokens[i].substring(0, length - 2);
            phrase.tokens[i].tag  = toTag(Integer.parseInt(tokens[i].substring(length - 1)));
          }
          phrase.matchCount  = Long.parseLong(parts[1]);
          phrase.volumeCount = Integer.parseInt(parts[2]);
          phrase.firstYear   = Integer.parseInt(parts[3]);
          phrase.lastYear    = Integer.parseInt(parts[4]);
          phrase.relativeId  = Integer.parseInt(parts[5]);
          phrase.score       = Double.parseDouble(parts[6]);
          phrases.add(phrase);
        }
        response.phrases = phrases.toArray(new Phrase[0]);
      }
    }
    return response;
  }

  private static Status toStatus(int httpResponseCode) {
    switch (httpResponseCode) {
      case 200: return Status.OK;
      case 400: return Status.BAD_REQUEST;
      case 402: return Status.PAYMENT_REQUIRED;
      case 405: return Status.METHOD_NOT_ALLOWED;
      case 429: return Status.TOO_MANY_REQUESTS;
      case 500: return Status.SERVER_ERROR;
      default:  throw new IllegalArgumentException();
    }
  }
  
  private static String toString(Corpus corpus) {
    switch (corpus) {
      case ENGLISH_US: return "eng-us";
      case ENGLISH_GB: return "eng-gb";
      case SPANISH:    return "spa";
      case FRENCH:     return "fre";
      case GERMAN:     return "ger";
      case RUSSIAN:    return "rus";
      case CHINESE:    return "chi";
      default:         throw new IllegalArgumentException();
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
    sb.append("http://phrasefinder.io/search?format=tsv")
      .append("&query=").append(URLEncoder.encode(query,
          java.nio.charset.StandardCharsets.UTF_8.toString()))
      .append("&corpus=").append(toString(options.corpus))
      .append("&nmin=").append(options.nmin)
      .append("&nmax=").append(options.nmax)
      .append("&topk=").append(options.topk);
    if (options.key != null) {
      sb.append("&key=").append(options.key);
    }
    return new URL(sb.toString());
  }
  
}
