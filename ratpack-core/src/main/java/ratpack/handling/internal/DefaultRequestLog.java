/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.handling.internal;

import com.google.common.net.HostAndPort;
import ratpack.auth.UserIdentifier;
import ratpack.handling.Context;
import ratpack.handling.RequestId;
import ratpack.handling.RequestLog;
import ratpack.handling.RequestOutcome;
import ratpack.http.*;
import ratpack.http.internal.HttpHeaderConstants;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DefaultRequestLog implements RequestLog {

  private static final String DEFAULT_FORMAT = "dd/MMM/yyyy:HH:mm:ss Z";
  //TODO is systemDefault here correct or should we allow specifying the zoneId for the log?
  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT).withZone(ZoneId.systemDefault());

  @Override
  public String format(Context ctx, RequestOutcome outcome) {
    Request request = outcome.getRequest();
    SentResponse response = outcome.getResponse();
    String responseSize = "-";
    String contentLength = response.getHeaders().get(HttpHeaderConstants.CONTENT_LENGTH);
    if (contentLength != null) {
      responseSize = contentLength;
    }

    StringBuilder logLine = new StringBuilder()
      .append(
        ncsaLogFormat(
          request.getRemoteAddress(),
          "-",
          request.maybeGet(UserIdentifier.class),
          request.getTimestamp(),
          request.getMethod(),
          "/" + request.getPath(),
          request.getProtocol(),
          outcome.getResponse().getStatus(),
          responseSize));

    request.maybeGet(RequestId.class).ifPresent(id1 -> {
      logLine.append(" id=");
      logLine.append(id1.getId());
    });
    return logLine.toString();
  }

  String ncsaLogFormat(HostAndPort client, String rfc1413Ident, Optional<UserIdentifier> userId, Instant timestamp, HttpMethod method, String uri, String httpProtocol, Status status, String responseSize) {
    return String.format("%s %s %s [%s] \"%s %s %s\" %d %s",
      client.getHostText(),
      rfc1413Ident,
      userId.isPresent() ? userId.get().getUserIdentifier() : "-",
      formatter.format(timestamp),
      method.getName(),
      uri,
      httpProtocol,
      status.getCode(),
      responseSize);
  }
}
