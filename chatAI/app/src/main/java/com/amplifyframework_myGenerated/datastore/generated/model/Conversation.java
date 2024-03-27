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

/** This is an auto generated class representing the Conversation type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Conversations", authRules = {
  @AuthRule(allow = AuthStrategy.PRIVATE, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class Conversation implements Model {
  public static final QueryField ID = field("Conversation", "id");
  public static final QueryField NAME = field("Conversation", "name");
  public static final QueryField RECIPIENT = field("Conversation", "recipient");
  public static final QueryField SENDER = field("Conversation", "sender");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String name;
  private final @ModelField(targetType="String", isRequired = true) String recipient;
  private final @ModelField(targetType="String", isRequired = true) String sender;
  public String getId() {
      return id;
  }
  
  public String getName() {
      return name;
  }
  
  public String getRecipient() {
      return recipient;
  }
  
  public String getSender() {
      return sender;
  }

  private Conversation(String id, String name, String recipient, String sender) {
    this.id = id;
    this.name = name;
    this.recipient = recipient;
    this.sender = sender;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Conversation conversation = (Conversation) obj;
      return ObjectsCompat.equals(getId(), conversation.getId()) &&
              ObjectsCompat.equals(getName(), conversation.getName()) &&
              ObjectsCompat.equals(getRecipient(), conversation.getRecipient()) &&
              ObjectsCompat.equals(getSender(), conversation.getSender());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getName())
      .append(getRecipient())
      .append(getSender())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Conversation {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("name=" + String.valueOf(getName()) + ", ")
      .append("recipient=" + String.valueOf(getRecipient()) + ", ")
      .append("sender=" + String.valueOf(getSender()) + ", ")
      .append("}")
      .toString();
  }
  
  public static NameStep builder() {
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
  public static Conversation justId(String id) {
    return new Conversation(
      id,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      name,
      recipient,
      sender);
  }
  public interface NameStep {
    RecipientStep name(String name);
  }
  

  public interface RecipientStep {
    SenderStep recipient(String recipient);
  }
  

  public interface SenderStep {
    BuildStep sender(String sender);
  }
  

  public interface BuildStep {
    Conversation build();
    BuildStep id(String id);
  }
  

  public static class Builder implements NameStep, RecipientStep, SenderStep, BuildStep {
    private String id;
    private String name;
    private String recipient;
    private String sender;
    @Override
     public Conversation build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Conversation(
          id,
          name,
          recipient,
          sender);
    }
    
    @Override
     public RecipientStep name(String name) {
        Objects.requireNonNull(name);
        this.name = name;
        return this;
    }
    
    @Override
     public SenderStep recipient(String recipient) {
        Objects.requireNonNull(recipient);
        this.recipient = recipient;
        return this;
    }
    
    @Override
     public BuildStep sender(String sender) {
        Objects.requireNonNull(sender);
        this.sender = sender;
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
    private CopyOfBuilder(String id, String name, String recipient, String sender) {
      super.id(id);
      super.name(name)
        .recipient(recipient)
        .sender(sender);
    }
    
    @Override
     public CopyOfBuilder name(String name) {
      return (CopyOfBuilder) super.name(name);
    }
    
    @Override
     public CopyOfBuilder recipient(String recipient) {
      return (CopyOfBuilder) super.recipient(recipient);
    }
    
    @Override
     public CopyOfBuilder sender(String sender) {
      return (CopyOfBuilder) super.sender(sender);
    }
  }
  
}
