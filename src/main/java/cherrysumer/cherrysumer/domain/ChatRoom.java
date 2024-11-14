package cherrysumer.cherrysumer.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "ChatRoom")
@DynamicUpdate
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(value = {AuditingEntityListener.class})
@NoArgsConstructor
@AllArgsConstructor

public class ChatRoom extends BaseEntity{
    @EqualsAndHashCode.Include
    @Id
    @Column(name = "id")
    private String id;

    //단방향
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "lastChatMesgId")
    private ChatMessage lastChatMesg;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "ChatRoom_Users",
            joinColumns = @JoinColumn(name = "chatRoomId"),
            inverseJoinColumns = @JoinColumn(name = "userId"))
    private Set<User> chatRoomMembers = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "ChatRoom_Posts",
            joinColumns = @JoinColumn(name = "chatRoomId"),
            inverseJoinColumns = @JoinColumn(name = "postId")
    )
    private Set<Post> associatedPosts = new HashSet<>();


    public static ChatRoom create() {

        ChatRoom room = new ChatRoom();
        room.setId(UUID.randomUUID().toString());

        return room;
    }

    public void addMembersAndPost(User roomMaker, User guest, Post post) {
        this.chatRoomMembers.add(roomMaker);
        this.chatRoomMembers.add(guest);
        this.associatedPosts.add(post);
    }
}
