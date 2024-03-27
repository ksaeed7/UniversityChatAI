package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Message type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Messages", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Message implements Model {
  public static final QueryField ID = field("Message", "id");
  public static final QueryField CONTENT = field("Message", "content");
  public static final QueryField CONVERSATION_ID = field("Message", "conversationID");
  public static final QueryField DATE = field("Message", "date");
  public static final QueryField MESSAGE_SENDER = field("Message", "message_sender");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String content;
  private final @ModelField(targetType="ID", isRequired = true) String conversationID;
  private final @ModelField(targetType="AWSDateTime") Temporal.DateTime date;
  private final @ModelField(targetType="String", isRequired = true) String message_sender;
  public String getId() {
      return id;
  }
  
  public String getContent() {
      return content;
  }
  
  public String getConversationId() {
      return conversationID;
  }
  
  public Temporal.DateTime getDate() {
      return date;
  }
  
  public String getMessageSender() {
      return message_sender;
  }

  
  private Message(String id, String content, String conversationID, Temporal.DateTime date, String message_sender) {
    this.id = id;
    this.content = content;
    this.conversationID = conversationID;
    this.date = date;
    this.message_sender = message_sender;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Message message = (Message) obj;
      return ObjectsCompat.equals(getId(), message.getId()) &&
              ObjectsCompat.equals(getContent(), message.getContent()) &&
              ObjectsCompat.equals(getConversationId(), message.getConversationId()) &&
              ObjectsCompat.equals(getDate(), message.getDate()) &&
              ObjectsCompat.equals(getMessageSender(), message.getMessageSender());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getContent())
      .append(getConversationId())
      .append(getDate())
      .append(getMessageSender())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Message {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("content=" + String.valueOf(getContent()) + ", ")
      .append("conversationID=" + String.valueOf(getConversationId()) + ", ")
      .append("date=" + String.valueOf(getDate()) + ", ")
      .append("message_sender=" + String.valueOf(getMessageSender()) + ", ")
      .append("}")
      .toString();
  }
  
  public static ContentStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Message justId(String id) {
    return new Message(
      id,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      content,
      conversationID,
      date,
      message_sender);
  }
  public interface ContentStep {
    ConversationIdStep content(String content);
  }
  

  public interface ConversationIdStep {
    MessageSenderStep conversationId(String conversationId);
  }
  

  public interface MessageSenderStep {
    BuildStep messageSender(String messageSender);
  }
  

  public interface BuildStep {
    Message build();
    BuildStep id(String id);
    BuildStep date(Temporal.DateTime date);
  }
  

  public static class Builder implements ContentStep, ConversationIdStep, MessageSenderStep, BuildStep {
    private String id;
    private String content;
    private String conversationID;
    private String message_sender;
    private Temporal.DateTime date;
    @Override
     public Message build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Message(
          id,
          content,
          conversationID,
          date,
          message_sender);
    }
    
    @Override
     public ConversationIdStep content(String content) {
        Objects.requireNonNull(content);
        this.content = content;
        return this;
    }
    
    @Override
     public MessageSenderStep conversationId(String conversationId) {
        Objects.requireNonNull(conversationId);
        this.conversationID = conversationId;
        return this;
    }
    
    @Override
     public BuildStep messageSender(String messageSender) {
        Objects.requireNonNull(messageSender);
        this.message_sender = messageSender;
        return this;
    }
    
    @Override
     public BuildStep date(Temporal.DateTime date) {
        this.date = date;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String content, String conversationId, Temporal.DateTime date, String messageSender) {
      super.id(id);
      super.content(content)
        .conversationId(conversationId)
        .messageSender(messageSender)
        .date(date);
    }
    
    @Override
     public CopyOfBuilder content(String content) {
      return (CopyOfBuilder) super.content(content);
    }
    
    @Override
     public CopyOfBuilder conversationId(String conversationId) {
      return (CopyOfBuilder) super.conversationId(conversationId);
    }
    
    @Override
     public CopyOfBuilder messageSender(String messageSender) {
      return (CopyOfBuilder) super.messageSender(messageSender);
    }
    
    @Override
     public CopyOfBuilder date(Temporal.DateTime date) {
      return (CopyOfBuilder) super.date(date);
    }
  }
  
}
