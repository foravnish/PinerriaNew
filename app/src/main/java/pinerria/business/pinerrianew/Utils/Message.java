package pinerria.business.pinerrianew.Utils;

/**
 * Created by Belal on 5/29/2016.
 */
public class Message {
    private int usersId;
    private String message;
    private String sentAt;

    public Message(int usersId, String message, String sentAt) {
        this.usersId = usersId;
        this.message = message;
        this.sentAt = sentAt;
    }

    public int getUsersId() {
        return usersId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }


}