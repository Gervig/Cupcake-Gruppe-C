import app.entities.User;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class IntegrationTest
{
    boolean actual;
    boolean expected;
    IntegrationTest it = new IntegrationTest();
    BigDecimal bd = new BigDecimal(500.00);
    User u1 = new User(1, "Casper", "Gervig", "gervig91", "gervig@gmail.com", "1234", bd, 2400, "customer");
}
