import java.net.*;

public class SrvUtil {

    static InetAddress findAddress() throws SocketException, UnknownHostException {
        var en0 = NetworkInterface.getByName("en0");
        return en0.inetAddresses()
                .filter(a -> a instanceof Inet4Address)
                .findFirst()
                .orElse(InetAddress.getLocalHost());
    }
}
