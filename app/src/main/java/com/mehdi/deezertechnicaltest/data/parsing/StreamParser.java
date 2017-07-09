package com.mehdi.deezertechnicaltest.data.parsing;

import android.support.annotation.WorkerThread;
import com.mehdi.deezertechnicaltest.data.exception.ParsingException;
import java.io.InputStream;

/**
 * Contract use to parse data.
 */
public interface StreamParser<T> {
  /**
   * Parse data.
   *
   * @param stream Containing data to parse.
   */
  @WorkerThread
  T parse(InputStream stream) throws ParsingException;
}
