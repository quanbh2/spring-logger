# spring-logger
Logger for Spring

# Các config trong file logback.xml

1. packagingData="true"

As of version 1.1.4, packaging data is disabled by default.

If instructed to do so, logback can include packaging data for each line of the stack trace lines it outputs. Packaging data consists of the name and version of the jar file whence the class of the stack trace line originated. Packaging data can be very useful in identifying software versioning issues. However, it is rather expensive to compute, especially in application where exceptions are thrown frequently. Here is a sample output:

14:28:48.835 [btpool0-7] INFO  c.q.l.demo.prime.PrimeAction - 99 is not a valid value
java.lang.Exception: 99 is invalid
  at ch.qos.logback.demo.prime.PrimeAction.execute(PrimeAction.java:28) [classes/:na]
  at org.apache.struts.action.RequestProcessor.processActionPerform(RequestProcessor.java:431) [struts-1.2.9.jar:1.2.9]
  at org.apache.struts.action.RequestProcessor.process(RequestProcessor.java:236) [struts-1.2.9.jar:1.2.9]
  at org.apache.struts.action.ActionServlet.doPost(ActionServlet.java:432) [struts-1.2.9.jar:1.2.9]
  at javax.servlet.http.HttpServlet.service(HttpServlet.java:820) [servlet-api-2.5-6.1.12.jar:6.1.12]

2. scan="true" scanPeriod="10 seconds"

If instructed to do so, logback-classic will scan for changes in its configuration file and automatically reconfigure itself when the configuration file changes. In order to instruct logback-classic to scan for changes in its configuration file and to automatically re-configure itself set the scan attribute of the <configuration> element to true, as shown next.

=> Thay đổi cấu hình file logback.xml realtime ( khi dùng IDE, sửa file logback.xml trong thư mục target)

3. <include resource="org/springframework/boot/logging/logback/base.xml"/>

Khi sử dụng file logback.xml thì cấu hình mặc định Logback của spring boot sẽ bị override, spring boot sẽ sử dụng cấu hình trong file logback.xml. Nếu ta muốn bao gồm cấu hình mặc định của spring boot thì ta cần thêm config dưới đây bên trong thẻ <configuration>
Log sẽ được in ra 2 lần nếu dùng cả 2 cấu hình trên.

4. appender
Nó sẽ tạo ra một appender của class ConsoleAppender, config này sẽ ghi message ra màn hình console dạng System.out.print. Cùng với đó là một pattern được setup cho trình ghi console tuân thủ các ký hiệu:

%d : thời gian ghi message, có thể chấp nhận tất các các định dạng SimperDateFormat cho phép
%thread : tên thread ghi message
$-5level : level ghi log (các mức level có thể là trace, debug, info, warn, error)
%logger{36} : tên package class nơi log được ghi ra. Số 36 có ý nghĩa là lược ngắn tên package class trong trường hợp tên quá dài.
%M : tên của method nơi log được ghi ra
%msg: message chính
%n : ngắt dòng
r / relative	Outputs the number of milliseconds elapsed since the start of the application until the creation of the logging event.
%magenta() : đặt màu của message đầu ra trong dấu (), có thể chọn các màu khác
highlight() : đặt màu của message đầu ra trong dấu (), tùy thuộc vào level log (ví dụ ERROR là màu đỏ)

Cấp độ log level là TRACE -> DUBUG -> INFO -> WARN -> ERROR.

5.

Nếu muốn ghi log trong từng class ở level khác với root thì ta có thể xác định config ghi log cho từng class như sau

<logger name="com.lankydan.service.MyServiceImpl" level="debug">
  <appender-ref ref="STDOUT" />
</logger> 

Như ta thấy thì mỗi message được ghi ra 2 lần, rõ ràng đây ko phải điều ta muốn. Để xử lý vấn đề này ta cần sử dụng thêm thông tin additivity="false vào cấu hình

<logger name="com.lankydan.service.MyServiceImpl" additivity="false" level="debug">
  <appender-ref ref="STDOUT" />
</logger>

Thậm chí ngay cả khi setting root level là ERROR, bởi setting class level là DEBUG nên nó vẫn override setting ở root level và ghi ra log từ level DEBUG trở lên như bên trên.

Package level logging cũng có thể được định nghĩa bằng cách sử dụng package name thay vì class name. Ví dụ

<logger name="com.lankydan.service" additivity="false" level="debug">
  <appender-ref ref="STDOUT" />
</logger> 

6. FileAppender

Để ghi log vào file, ta cần config FileAppender . Với việc sử dụng FileAppender, tất cả log sẽ được ghi vào một file, điều này có thể dẫn đến file sẽ rất lớn qua thời gian. Một cách khác là ta sử dụng RollingFileAppender

Config RollingFileAppender sẽ ghi log vào các file khác nhau tùy vào cấu hình, có thể là là tách ghi file log theo ngày, hoặc theo tháng (miễn là định dạng trong %d phù hợp với SimpleDateFormat cho phép, định dạng format trong cặp thẻ %d cũng sẽ quy định thời gian rolling, ví dụ "dd-MM-yyyy" sẽ rolling theo ngày hoặc "MM-yyyy" sẽ rolling theo tháng) hoặc khi kích thước file đạt tới giới hạn nào đó, hoặc kết hợp cả hai.
TimeBasedRollingPolicy dưới đây sẽ tạo một file log mới theo ngày. Tên file log sẽ kèm suffix là ngày ghi log.

Ví dụ trên cũng setting thêm các tham số maxHistory là thời gian chỉ định số ngày lưu trữ file trước khi chúng bị tự động xóa tính từ ngày ghi file log đó. totalSizeCap giới hạn kích thước tối đa của tất cả các file log lưu trữ, nó yêu cầu thuộc tính maxHistory đi kèm.

7. springProfile

Một tính năng hữu ích nữa mà Spring Boot cung cấp khi sử dụng Logback là khả năng phân tách cấu hình giữa các môi trường. Vì vậy, nếu bạn muốn lưu vào file và in ra console trong môi trường develop của mình nhưng chỉ ghi ra file trong môi trường production thì điều này có thể đạt được một cách dễ dàng
<?xml version="1.0" encoding="UTF-8"?>
<configuration>

  <!-- config for STDOUT and SAVE-TO-FILE -->

  <springProfile name="dev">
    <root level="info">
      <appender-ref ref="STDOUT"/>
      <appender-ref ref="SAVE-TO-FILE"/>
    </root>
    <logger name="com.lankydan.service.MyServiceImpl" additivity="false" level="debug">
      <appender-ref ref="STDOUT"/>
      <appender-ref ref="SAVE-TO-FILE"/>
    </logger>
  </springProfile>

  <springProfile name="prod">
    <root level="info">
      <appender-ref ref="SAVE-TO-FILE"/>
    </root>
    <logger name="com.lankydan.service.MyServiceImpl" additivity="false" level="error">
      <appender-ref ref="SAVE-TO-FILE"/>
    </logger>
  </springProfile>

</configuration>    

8. ThresholdFilter

=> Config này là filter của trình ghi log 
<filter class="ch.qos.logback.classic.filter.ThresholdFilter">
      <level>WARN</level>
</filter>
=> Còn trình ghi log in log nào ra là do log level quyết định
    
    

The level assigned for the logger is the one used by your logger, whereas the level assigned inside the filter ch.qos.logback.classic.filter.ThresholdFilter is the level from which this appender will be logging stuff, for more details.

