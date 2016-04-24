package uk.co.sleonard.unison.input;

import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import junit.framework.TestCase;
import uk.co.sleonard.unison.datahandling.DAO.NewsGroup;
import uk.co.sleonard.unison.gui.UNISoNController;

public class NewsGroupReaderTest extends TestCase {
    
    class TestFolder extends Folder {
        
        private ArrayList<Message> messages;
        
        protected TestFolder(Store arg0) {
            super(arg0);
            messages = new ArrayList<Message>();
//            messages.add(new NNTPMessage());
        }
        
        @Override
        public void appendMessages(Message[] arg0) throws MessagingException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public void close(boolean arg0) throws MessagingException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public boolean create(int arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public boolean delete(boolean arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public boolean exists() throws MessagingException {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public Message[] expunge() throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public Folder getFolder(String arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public String getFullName() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public Message getMessage(int arg0) throws MessagingException {
            return messages.get(arg0);
        }
        
        @Override
        public int getMessageCount() throws MessagingException {
            return messages.size();
        }
        
        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public Folder getParent() throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public Flags getPermanentFlags() {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public char getSeparator() throws MessagingException {
            // TODO Auto-generated method stub
            return 0;
        }
        
        @Override
        public int getType() throws MessagingException {
            // TODO Auto-generated method stub
            return 0;
        }
        
        @Override
        public boolean hasNewMessages() throws MessagingException {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public boolean isOpen() {
            // TODO Auto-generated method stub
            return false;
        }
        
        @Override
        public Folder[] list(String arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public void open(int arg0) throws MessagingException {
            // TODO Auto-generated method stub
            
        }
        
        @Override
        public boolean renameTo(Folder arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return false;
        }
        
    }
    
    class TestNewsGroupReader extends NewsGroupReader {
        public TestNewsGroupReader(UNISoNController controller) {
			super(controller);
			// TODO Auto-generated constructor stub
		}

		@Override
        public void connectToNewsGroup(String host, NewsGroup newsgroup)
        throws NoSuchProviderException, MessagingException,
                UnknownHostException {
            store = new TestStore(null, null);
            folder = new TestFolder(store);
            // TODO Auto-generated method stub
            super.connectToNewsGroup(host, newsgroup);
        }
    }
    
    class TestStore extends Store {
        
        protected TestStore(Session arg0, URLName arg1) {
            super(arg0, arg1);
            // TODO Auto-generated constructor stub
        }
        
        @Override
        public Folder getDefaultFolder() throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public Folder getFolder(String arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
        @Override
        public Folder getFolder(URLName arg0) throws MessagingException {
            // TODO Auto-generated method stub
            return null;
        }
        
    }
    
    private TestNewsGroupReader test;
    
    @Override
    protected void setUp() throws Exception {
        test = new TestNewsGroupReader(null);
    }
    
    public void testSaveToDB() throws Exception {
        
    }
}
