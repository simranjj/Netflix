package service;

import controller.EpisodesJpaController;
import controller.SeriesJpaController;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.ws.soap.MTOM;
import model.Episodes;
import model.Series;

/**
 *
 * @author simra
 */
@MTOM(enabled = true, threshold = 0)
@WebService(serviceName = "NetflixServiceService",wsdlLocation = "https://project-eam.herokuapp.com/NetflixServiceService")
@HandlerChain(file = "NetflixServiceService_handler.xml")
public class NetflixService {

    @WebMethod
    public boolean CreateSeries(@WebParam(name = "series") Series series) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        SeriesJpaController seriesRepo = new SeriesJpaController(emf);
        series.setSeriesid(BigDecimal.valueOf(new Date().getTime()));
        seriesRepo.create(series);
        return true;
    }

    @WebMethod
    public boolean CreateEpisode(@WebParam(name = "episode") Episodes episode) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        EpisodesJpaController episodeRepo = new EpisodesJpaController(emf);
        episode.setEpisodeid(BigDecimal.valueOf(new Date().getTime()));
        episodeRepo.create(episode);
        return true;
    }

    @WebMethod
    public boolean EditSeries(@WebParam(name = "series") Series series) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        SeriesJpaController seriesRepo = new SeriesJpaController(emf);
        seriesRepo.edit(series);

        return true;
    }

    @WebMethod
    public boolean EditEpisode(@WebParam(name = "episode") Episodes episode) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        EpisodesJpaController episodeRepo = new EpisodesJpaController(emf);
        episodeRepo.edit(episode);
        return true;
    }

    @WebMethod
    public boolean DeleteSeries(@WebParam(name = "seriesid") BigDecimal seriesid) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        SeriesJpaController seriesRepo = new SeriesJpaController(emf);

        List<Episodes> epiList = ListEpisodeBySeriesId(seriesid);
        for (Episodes e : epiList) {
            DeleteEpisode(e.getEpisodeid());
        }

        seriesRepo.destroy(seriesid);

        return true;
    }

    @WebMethod
    public boolean DeleteEpisode(@WebParam(name = "episodeid") BigDecimal episodeid) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        EpisodesJpaController episodeRepo = new EpisodesJpaController(emf);
        episodeRepo.destroy(episodeid);

        return true;
    }

    @WebMethod
    public Series FindSeries(@WebParam(name = "episodeid") BigDecimal seriesid) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        SeriesJpaController seriesRepo = new SeriesJpaController(emf);
        Series series = seriesRepo.findSeries(seriesid);

        return series;
    }

    @WebMethod
    public Episodes FindEpisode(@WebParam(name = "episodeid") BigDecimal episodeId) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        EpisodesJpaController episodeRepo = new EpisodesJpaController(emf);
        Episodes episodes = episodeRepo.findEpisodes(episodeId);

        return episodes;
    }

    @WebMethod
    public List<Episodes> ListEpisodeBySeriesId(@WebParam(name = "seriesid") BigDecimal seriesid) throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        EpisodesJpaController episodeRepo = new EpisodesJpaController(emf);

        List<Episodes> episodesList = episodeRepo.findEpisodesBySeries(seriesid);

        return episodesList;
    }

    @WebMethod
    public List<Series> ListSeries() throws Exception {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("NetflixSOAP_PU");
        SeriesJpaController seriesRepo = new SeriesJpaController(emf);

        List<Series> seriesList = seriesRepo.findSeriesEntities();
        return seriesList;
    }

}
