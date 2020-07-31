package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author simra
 */

@Entity
@Table(name = "EPISODES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Episodes.findAll", query = "SELECT e FROM Episodes e"),
    @NamedQuery(name = "Episodes.findByEpisodeid", query = "SELECT e FROM Episodes e WHERE e.episodeid = :episodeid"),
    @NamedQuery(name = "Episodes.findByReleasedate", query = "SELECT e FROM Episodes e WHERE e.releasedate = :releasedate"),
    @NamedQuery(name = "Episodes.findByTimelength", query = "SELECT e FROM Episodes e WHERE e.timelength = :timelength"),
    @NamedQuery(name = "Episodes.findByTitle", query = "SELECT e FROM Episodes e WHERE e.title = :title")})
public class Episodes implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "EPISODEID")
    private BigDecimal episodeid;
    @Column(name = "RELEASEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedate;
    @Column(name = "TIMELENGTH")
    private BigDecimal timelength;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TITLE")
    private String title;
    @JoinColumn(name = "SERIESID", referencedColumnName = "SERIESID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Series seriesid;

    public Episodes() {
    }

    public Episodes(BigDecimal episodeid) {
        this.episodeid = episodeid;
    }

    public Episodes(BigDecimal episodeid, String title) {
        this.episodeid = episodeid;
        this.title = title;
    }
public Episodes(BigDecimal episodeid,String title, Series seriesid) {
        this.episodeid = episodeid;

        this.title = title;
        this.seriesid = seriesid;
    }

    

    public BigDecimal getEpisodeid() {
        return episodeid;
    }

    public void setEpisodeid(BigDecimal episodeid) {
        this.episodeid = episodeid;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public BigDecimal getTimelength() {
        return timelength;
    }

    public void setTimelength(BigDecimal timelength) {
        this.timelength = timelength;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Series getSeriesid() {
        return seriesid;
    }

    public void setSeriesid(Series seriesid) {
        this.seriesid = seriesid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (episodeid != null ? episodeid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Episodes)) {
            return false;
        }
        Episodes other = (Episodes) object;
        if ((this.episodeid == null && other.episodeid != null) || (this.episodeid != null && !this.episodeid.equals(other.episodeid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Episodes[ episodeid=" + episodeid + " ]";
    }

}
