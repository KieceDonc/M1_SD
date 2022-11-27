package com.perfect.bank.messages;

import java.io.Serializable;

public class Messages {
    public interface Message extends Serializable {
    }

    public static class GetUID implements Message {
        public GetUID() {
        }
    }
}
