package panic.io;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class CustomLoggerFormatter extends Formatter {
  
  /**
   * "\n" doesn't seem to work to break the line, so this value registers as a line break on all
   * systems.
   */
  private static final String newline = System.getProperty("line.separator");
  
  /**
   * Used to get and format the current time.
   */
  private static final DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS");
  
  /**
   * Correctly format a logged message as desired. 
   */
  @Override
  public String format(LogRecord record) {
    StringBuilder message = new StringBuilder(1000);
    message.append(formatter.format(System.currentTimeMillis()));
    message.append(" - ");
    //maybe add level management here?
    message.append(formatMessage(record));
    message.append(newline);
    return message.toString();
  }

  
  
}
