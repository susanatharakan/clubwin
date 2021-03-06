
package co.uk.mcb.clubwin.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
public class ClubWin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String winCode;

    public ClubWin(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWinCode() {
        return winCode;
    }

    public void setWinCode(String winCode) {
        this.winCode = winCode;
    }

}
