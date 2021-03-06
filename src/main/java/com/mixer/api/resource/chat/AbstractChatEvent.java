package com.mixer.api.resource.chat;

import com.google.gson.annotations.SerializedName;
import com.mixer.api.resource.chat.events.ChatDisconnectEvent;
import com.mixer.api.resource.chat.events.DeleteMessageEvent;
import com.mixer.api.resource.chat.events.IncomingMessageEvent;
import com.mixer.api.resource.chat.events.IncomingWidgetEvent;
import com.mixer.api.resource.chat.events.PollEndEvent;
import com.mixer.api.resource.chat.events.PollStartEvent;
import com.mixer.api.resource.chat.events.PurgeMessageEvent;
import com.mixer.api.resource.chat.events.StatusEvent;
import com.mixer.api.resource.chat.events.UserJoinEvent;
import com.mixer.api.resource.chat.events.UserLeaveEvent;
import com.mixer.api.resource.chat.events.UserUpdateEvent;
import com.mixer.api.resource.chat.events.WelcomeEvent;

public abstract class AbstractChatEvent<T extends AbstractChatEvent.EventData> extends AbstractChatDatagram {
    public AbstractChatEvent() {
        this.type = Type.EVENT;
    }

    public EventType event;
    public T data;

    public static abstract class EventData {}
    public static enum EventType {
        @SerializedName("WidgetMessage") WIDGET_MESSAGE (IncomingWidgetEvent.class),
        @SerializedName("ChatMessage") CHAT_MESSAGE (IncomingMessageEvent.class),
        @SerializedName("DeleteMessage") DELETE_MESSAGE (DeleteMessageEvent.class),
        @SerializedName("PollStart") POLL_START (PollStartEvent.class),
        @SerializedName("PollEnd") POLL_END (PollEndEvent.class),
        @SerializedName("Stats") STATS (StatusEvent.class),
        @SerializedName("UserJoin") USER_JOIN (UserJoinEvent.class),
        @SerializedName("UserLeave") USER_LEAVE (UserLeaveEvent.class),
        @SerializedName("disconnect") DISCOUNNECT (ChatDisconnectEvent.class),
        @SerializedName("WelcomeEvent") WELCOME (WelcomeEvent.class),
        @SerializedName("UserUpdate") USER_UPDATE (UserUpdateEvent.class),
        @SerializedName("PurgeMessage") PURGE_MESSAGE (PurgeMessageEvent.class);

        private final Class<? extends AbstractChatEvent> correspondingClass;

        private EventType(Class<? extends AbstractChatEvent> correspondingClass) {
            this.correspondingClass = correspondingClass;
        }

        public static EventType fromSerializedName(String name) {
            if (name == null) return null;

            for (EventType type : EventType.values()) {
                try {
                    String serializedName = type.getClass().getField(type.name())
                                                           .getAnnotation(SerializedName.class).value();
                    if (name.equals(serializedName)) {
                        return type;
                    }
                } catch (NoSuchFieldException e) {
                    return null;
                }
            }

            return null;
        }

        public Class<? extends AbstractChatEvent> getCorrespondingClass() {
            return this.correspondingClass;
        }
    }
}
