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

import java.util.Objects;

/**
 * Result represents a search result.
 */
public class Result {

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
     * A required parameter was missing or had an invalid value.
     */
    BAD_REQUEST,

    /**
     * The service is currently down. Please try again later.
     */
    BAD_GATEWAY;

    public static Status fromHttpStatusCode(int httpStatusCode) {
      switch (httpStatusCode) {
        case 200:
          return Status.OK;
        case 400:
          return Status.BAD_REQUEST;
        case 502:
          return Status.BAD_GATEWAY;
        default:
          throw new IllegalArgumentException(
              String.format("Unexpected HTTP status code: %d", httpStatusCode));
      }
    }
  }

  private final Status status;
  private final Phrase[] phrases;
  
  protected Result(Status status, Phrase[] phrases) {
    this.status = Objects.requireNonNull(status);
    this.phrases = Objects.requireNonNull(phrases);
  }

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
