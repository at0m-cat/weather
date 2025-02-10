package matveyodintsov.weather.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "sessions")
public class Sessions {

    public Sessions() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users userId;

    @Column(name = "expires_at")
    private Date expiresat;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Account getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public Date getExpiresat() {
        return expiresat;
    }

    public void setExpiresat(Date expiresat) {
        this.expiresat = expiresat;
    }
}
