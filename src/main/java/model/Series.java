package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author simra
 */

@Entity
@Table(name = "SERIES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Series.findAll", query = "SELECT s FROM Series s"),
    @NamedQuery(name = "Series.findBySeriesid", query = "SELECT s FROM Series s WHERE s.seriesid = :seriesid"),
    @NamedQuery(name = "Series.findByTitle", query = "SELECT s FROM Series s WHERE s.title = :title"),
    @NamedQuery(name = "Series.findByGenre", query = "SELECT s FROM Series s WHERE s.genre = :genre"),
    @NamedQuery(name = "Series.findByTimelength", query = "SELECT s FROM Series s WHERE s.timelength = :timelength"),
    @NamedQuery(name = "Series.findByCost", query = "SELECT s FROM Series s WHERE s.cost = :cost"),
    @NamedQuery(name = "Series.findByReleasedate", query = "SELECT s FROM Series s WHERE s.releasedate = :releasedate"),
    @NamedQuery(name = "Series.findByDistributor", query = "SELECT s FROM Series s WHERE s.distributor = :distributor"),
    @NamedQuery(name = "Series.findByRating", query = "SELECT s FROM Series s WHERE s.rating = :rating")})
public class Series implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SERIESID")
    private BigDecimal seriesid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 10)
    @Column(name = "GENRE")
    private String genre;
    @Column(name = "TIMELENGTH")
    private BigDecimal timelength;
    @Column(name = "COST")
    private BigDecimal cost;
    @Column(name = "RELEASEDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releasedate;
    @Size(max = 20)
        @Column(name = "DISTRIBUTOR")
    private String distributor;
    @Column(name = "RATING")
    private Integer rating;
    @Lob
    @Column(name = "POSTER")
    private byte[] poster;
    @OneToMany(mappedBy = "seriesid",fetch = FetchType.EAGER)
    private Collection<Episodes> episodesCollection;

    public Series() {
    }

    public Series(BigDecimal seriesid) {
        this.seriesid = seriesid;
    }

    public Series(BigDecimal seriesid, String title) {
        this.seriesid = seriesid;
        this.title = title;
    }

    public BigDecimal getSeriesid() {
        return seriesid;
    }

    public void setSeriesid(BigDecimal seriesid) {
        this.seriesid = seriesid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public BigDecimal getTimelength() {
        return timelength;
    }

    public void setTimelength(BigDecimal timelength) {
        this.timelength = timelength;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public Date getReleasedate() {
        return releasedate;
    }

    public void setReleasedate(Date releasedate) {
        this.releasedate = releasedate;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    @XmlTransient
    public Collection<Episodes> getEpisodesCollection() {
        return episodesCollection;
    }

    public void setEpisodesCollection(Collection<Episodes> episodesCollection) {
        this.episodesCollection = episodesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (seriesid != null ? seriesid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Series)) {
            return false;
        }
        Series other = (Series) object;
        if ((this.seriesid == null && other.seriesid != null) || (this.seriesid != null && !this.seriesid.equals(other.seriesid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Series[ seriesid=" + seriesid + " ]";
    }

}
