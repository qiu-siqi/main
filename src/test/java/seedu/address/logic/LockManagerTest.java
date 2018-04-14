package seedu.address.logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

//@@author qiu-siqi
public class LockManagerTest {

    @After
    public void tearDown() {
        LockManager.getInstance().initialize(LockManager.NO_PASSWORD);
    }

    @Test
    public void instantiate() throws Exception {
        LockManager.getInstance().initialize(CipherEngine.hashPassword("testing"));
        assertTrue(LockManager.getInstance().isLocked());
        assertFalse(LockManager.getInstance().hasLoggedIn());
        assertTrue(LockManager.getInstance().isPasswordProtected());
    }

    @Test
    public void lock() {
        LockManager.getInstance().lock();
        assertTrue(LockManager.getInstance().isLocked());

        // Locking again shouldn't change anything
        LockManager.getInstance().lock();
        assertTrue(LockManager.getInstance().isLocked());
    }

    @Test
    public void unlock() throws Exception {
        LockManager.getInstance().initialize(CipherEngine.hashPassword("testing"));
        assertFalse(LockManager.getInstance().hasLoggedIn());

        // Cannot unlock if wrong password
        assertFalse(LockManager.getInstance().unlock("other password"));

        // Correct password -> Unlocked
        assertEquals(LockManager.NO_PASSWORD, LockManager.getInstance().getPassword());
        assertTrue(LockManager.getInstance().unlock("testing"));
        assertTrue(LockManager.getInstance().hasLoggedIn());
        // password reflected after login
        assertEquals("testing", LockManager.getInstance().getPassword());

        // Cannot unlock if not locked
        assertFalse(LockManager.getInstance().unlock("testing"));
    }

    @Test
    public void changePassword() {
        LockManager.getInstance().initialize(LockManager.NO_PASSWORD);

        // Set a password
        assertTrue(LockManager.getInstance().setPassword(LockManager.NO_PASSWORD, "testing"));
        assertTrue(LockManager.getInstance().isPasswordProtected());
        assertEquals(LockManager.getInstance().getPassword(), "testing");

        // Change the password
        assertTrue(LockManager.getInstance().setPassword("testing", "testing2"));
        assertTrue(LockManager.getInstance().isPasswordProtected());
        assertEquals(LockManager.getInstance().getPassword(), "testing2");

        // Wrong old password -> Password not changed
        assertFalse(LockManager.getInstance().setPassword("wrongpw", "newpw"));
        assertEquals(LockManager.getInstance().getPassword(), "testing2");

        // Delete the password
        assertTrue(LockManager.getInstance().setPassword("testing2", LockManager.NO_PASSWORD));
        assertFalse(LockManager.getInstance().isPasswordProtected());
        assertEquals(LockManager.getInstance().getPassword(), LockManager.NO_PASSWORD);
    }
}
