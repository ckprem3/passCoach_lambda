package passcoach.checks;

import junit.framework.TestCase;

public class DBChecksTest extends TestCase {

    public void testCheckDictionary() {
        DBChecks dbChecks = new DBChecks(new MockLambdaLogger());
        dbChecks.checkDictionary("retek");
    }

    public void testSave() {
        DBChecks dbChecks = new DBChecks(new MockLambdaLogger());
        dbChecks.save("test1","some long text");
    }
}