package com.example.rxjavademo.ui.list

import android.os.Looper.getMainLooper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxjavademo.R
import com.example.rxjavademo.data.model.NewsResponse
import com.example.rxjavademo.data.rest.Errors
import com.example.rxjavademo.data.rest.NewsRepository
import com.example.rxjavademo.data.rest.NewsService
import com.example.rxjavademo.data.rest.Output
import com.example.rxjavademo.utils.API_KEY
import com.example.rxjavademo.utils.COUNTRY
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.Spy
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class NewListViewModelTest {

    private lateinit var viewModel: NewListViewModel

    val str =
        "{\"articles\":[{\"content\":\"The analysis is part of a wide-ranging set of surveys initiated by the C.D.C. to estimate how far the virus has spread. It found, for instance, that in South Florida as of April 10, under 2 percent o… [+1464 chars]\",\"description\":\"China says it has largely contained a recent outbreak in Beijing. In a disturbing parallel to H.I.V., the coronavirus can cause a depletion of important immune cells, researchers have found.\",\"publishedAt\":\"2020-06-27T16:25:09Z\",\"source\":{\"name\":\"New York Times\"},\"title\":\"Coronavirus Live Updates: Latest News and Analysis - The New York Times\",\"url\":\"https://www.nytimes.com/2020/06/27/world/coronavirus-updates.html\",\"urlToImage\":\"https://www.nytimes.com/newsgraphics/2020/04/09/corona-virus-social-images-by-section/assets/World_promo.jpg?u\\u003d1593276329693\"},{\"author\":\"Rowan Moore\",\"content\":\"Its a privilege and occasional frustration for graphic designers that they furnish the backgrounds of the lives of millions, without those millions always being aware that they have done so. So it wa… [+4340 chars]\",\"description\":\"The man who came up with the classic I love NY logo died on Friday at 91, leaving a rich legacy\",\"publishedAt\":\"2020-06-27T16:06:57Z\",\"source\":{\"name\":\"The Guardian\"},\"title\":\"Milton Glaser: graphic designer who created the look of the Sixties - The Guardian\",\"url\":\"https://www.theguardian.com/artanddesign/2020/jun/27/milton-glaser-why-the-new-york-graphic-designer-was-so-influential\",\"urlToImage\":\"https://i.guim.co.uk/img/media/69a7a45dc3a800cb5510cac450c0208f3e11b5b4/1_434_3399_2040/master/3399.jpg?width\\u003d1200\\u0026height\\u003d630\\u0026quality\\u003d85\\u0026auto\\u003dformat\\u0026fit\\u003dcrop\\u0026overlay-align\\u003dbottom%2Cleft\\u0026overlay-width\\u003d100p\\u0026overlay-base64\\u003dL2ltZy9zdGF0aWMvb3ZlcmxheXMvdG8tZGVmYXVsdC5wbmc\\u0026enable\\u003dupscale\\u0026s\\u003dd8b2dcf12ba896f0815e840b0a3af7f3\"},{\"author\":\"Sara Dorn\",\"content\":\"Lori Loughlin and Mossimo Giannulli have been pushed out of the exclusive Bel-Air Country Club where top members were enraged their recent federal guilty pleas turned the establishment into a place o… [+1829 chars]\",\"description\":\"Top members were enraged their recent federal guilty pleas turned the exclusive club into a “laughingstock” and haven for “felons.”\",\"publishedAt\":\"2020-06-27T15:44:06Z\",\"source\":{\"name\":\"Page Six\"},\"title\":\"Lori Loughlin, Mossimo Giannulli pushed out of Bel-Air Country Club - Page Six\",\"url\":\"https://pagesix.com/2020/06/27/bel-air-country-club-pushes-lori-loughlin-mossimo-giannulli-out/\",\"urlToImage\":\"https://pagesix.com/wp-content/uploads/sites/3/2020/05/loughlin-giannulli.jpg?quality\\u003d90\\u0026strip\\u003dall\\u0026w\\u003d1200\"},{\"author\":\"TMZ Staff\",\"content\":\"A Trader Joe\\u0027s was the backdrop for an explosion of anger by a customer who felt it was her right to shop without a face mask.\\r\\nThe woman came to the San Fernando Valley store Friday wearing a mask .… [+631 chars]\",\"description\":\"A Trader Joe\\u0027s was the backdrop for an explosion of anger by a customer who felt it was her right to shop without a face mask.\",\"publishedAt\":\"2020-06-27T15:24:00Z\",\"source\":{\"name\":\"TMZ\"},\"title\":\"Trader Joe\\u0027s Customer Goes Nuts Over Face Masks - TMZ\",\"url\":\"https://www.tmz.com/2020/06/27/trader-joes-customer-goes-nuts-over-face-masks/\",\"urlToImage\":\"https://imagez.tmz.com/image/59/16by9/2020/06/27/5957598a97394ccfaacab550f58cbb6e_xl.jpg\"},{\"author\":\"Kelly Tyko\",\"content\":\"E.Coli, metal and even a dead bat has have been found in recalled food. In fact, food recalls are increasing. Yet, that might actually be a good sign. Here\\u0027s why.\\r\\nUSA TODAY\\r\\nDays after health offici… [+4494 chars]\",\"description\":\"Fresh Express has recalled salad mixes sold at Walmart, Hy-Vee, Aldi and Jewel-Osco that may be linked to a Cyclospora outbreak that has sickened 206.\",\"publishedAt\":\"2020-06-27T15:08:17Z\",\"source\":{\"id\":\"usa-today\",\"name\":\"USA Today\"},\"title\":\"Walmart, Aldi recall some Fresh Express garden salad mixes potentially linked to growing Cyclospora outbreak - USA TODAY\",\"url\":\"https://www.usatoday.com/story/money/food/2020/06/27/salad-recall-2020-walmart-aldi-fresh-express-cyclospora-outbreak/3269765001/\",\"urlToImage\":\"https://www.gannett-cdn.com/presto/2020/06/20/USAT/39ab0f34-2d56-4e66-908e-04af41996efd-HyVeeGarden_SaladMix_FrontPanel.jpg_1_0.jpg?crop\\u003d499,281,x0,y154\\u0026width\\u003d1600\\u0026height\\u003d800\\u0026fit\\u003dbounds\"},{\"author\":\"Melissa Roberto\",\"content\":\"“The Young and the Restless” took home the win for best drama at the first virtual Daytime Emmys along with three acting trophies on Friday night, with some winners speaking out about racial injustic… [+3992 chars]\",\"description\":\"Alex Trebek, Ellen DeGeneres and \\u0027The Young and the Restless\\u0027 won big at the Daytime Emmys.\",\"publishedAt\":\"2020-06-27T14:53:12Z\",\"source\":{\"id\":\"fox-news\",\"name\":\"Fox News\"},\"title\":\"Alex Trebek, Ellen DeGeneres, \\u0027Young and the Restless\\u0027 win big at Daytime Emmys - Fox News\",\"url\":\"https://www.foxnews.com/entertainment/alex-trebek-ellen-degeneres-young-and-the-restless-win-daytime-emmys\",\"urlToImage\":\"https://static.foxnews.com/foxnews.com/content/uploads/2020/06/Alex-Trebek-Ellen-DeGeneres-AP.jpg\"},{\"author\":\"Isabel Vincent\",\"content\":\"Billionaire Mark Zuckerberg took a \$7.2 billion hit after several companies pulled their advertising from Facebook, citing the company’s failure to police hate speech and disinformation on its site.\\r… [+1264 chars]\",\"description\":\"The company’s shares fell 8.3 percent Friday, the most in three months after Unilever, one of the world’s largest advertisers said it would stop selling its products on Facebook, Bloomb…\",\"publishedAt\":\"2020-06-27T14:50:03Z\",\"source\":{\"name\":\"New York Post\"},\"title\":\"Mark Zuckerberg loses \$7.2B over major advertising boycotts on Facebook - New York Post \",\"url\":\"https://nypost.com/2020/06/27/mark-zuckerberg-loses-7-2b-over-ad-boycotts-on-facebook/\",\"urlToImage\":\"https://nypost.com/wp-content/uploads/sites/2/2020/06/mark-zuckerberg-4.jpg?quality\\u003d90\\u0026strip\\u003dall\\u0026w\\u003d1200\"},{\"author\":\"Rich Schapiro\",\"content\":\"Shortly after four Minneapolis police officers were fired over the death of George Floyd, the president of the city\\u0027s police union wrote a letter to his members signaling that he was working to resto… [+10192 chars]\",\"description\":\"Since 2006, eight Minneapolis police firings have been decided by arbitrators and all but two were reinstated, according to an NBC News review of records.\",\"publishedAt\":\"2020-06-27T14:45:54Z\",\"source\":{\"id\":\"nbc-news\",\"name\":\"NBC News\"},\"title\":\"How the officers charged in George Floyd\\u0027s death could get their jobs back - NBC News\",\"url\":\"https://www.nbcnews.com/news/us-news/how-officers-charged-george-floyd-s-death-could-get-their-n1232236\",\"urlToImage\":\"https://media1.s-nbcnews.com/j/newscms/2020_26/3392407/200624-chauvin-lane-kueng-thao-2x1-cover-al-1331_89e55cc5d142334d2015e669d2a13280.nbcnews-fp-1200-630.JPG\"},{\"author\":\"Analysis by Maeve Reston, CNN\",\"description\":\"President Donald Trump has made many questionable decisions in recent weeks as he looks ahead to his increasingly difficult reelection campaign. But his administration\\u0027s decision to forge ahead with its effort to invalidate the Affordable Care Act through the…\",\"publishedAt\":\"2020-06-27T14:42:49Z\",\"source\":{\"id\":\"cnn\",\"name\":\"CNN\"},\"title\":\"Trump\\u0027s effort to dismantle Obamacare during the pandemic might be his riskiest move yet - CNN\",\"url\":\"https://www.cnn.com/2020/06/27/politics/donald-trump-obamacare-pandemic-2020-election/index.html\",\"urlToImage\":\"https://cdn.cnn.com/cnnnext/dam/assets/200626152436-01-trump-american-workforce-meeting-super-tease.jpg\"},{\"author\":\"Guardian staff\",\"content\":\"Outrage has greeted media reports that say American intelligence officials believe a Russian military intelligence unit offered bounties to Taliban-linked militants for killing foreign soldiers in Af… [+3244 chars]\",\"description\":\"Fierce response from top Democrats after US intelligence finding was reportedly briefed to Trump in March, but the White House has yet to act\",\"publishedAt\":\"2020-06-27T14:41:32Z\",\"source\":{\"name\":\"The Guardian\"},\"title\":\"Outrage mounts over report Russia offered bounties to Afghanistan militants for killing US soldiers - The Guardian\",\"url\":\"https://www.theguardian.com/us-news/2020/jun/27/russia-offered-bounties-afghanistan-militants-killing-us-soldiers-report-outrage\",\"urlToImage\":\"https://i.guim.co.uk/img/media/9b1c76f57d0483b41c5e4952c1d1084f9d572203/0_55_3465_2079/master/3465.jpg?width\\u003d1200\\u0026height\\u003d630\\u0026quality\\u003d85\\u0026auto\\u003dformat\\u0026fit\\u003dcrop\\u0026overlay-align\\u003dbottom%2Cleft\\u0026overlay-width\\u003d100p\\u0026overlay-base64\\u003dL2ltZy9zdGF0aWMvb3ZlcmxheXMvdGctZGVmYXVsdC5wbmc\\u0026enable\\u003dupscale\\u0026s\\u003d7225476ab4c5aeaba373ae2b6a2b761a\"},{\"author\":\"\",\"content\":\"With inclement weather expected to roll through Cromwell, Connecticut, on Saturday, Round 3 action at the 2020 Travelers Championship will get an early start. Threesomes will begin going off split te… [+3262 chars]\",\"description\":\"The groups are out for Saturday at TPC River Highlands as the Travelers action heads into the weekend\",\"publishedAt\":\"2020-06-27T13:43:47Z\",\"source\":{\"name\":\"CBS Sports\"},\"title\":\"2020 Travelers Championship tee times, pairings: Complete field, schedule set for Saturday in Round 3 - CBS Sports\",\"url\":\"https://www.cbssports.com/golf/news/2020-travelers-championship-tee-times-pairings-complete-field-schedule-set-for-saturday-in-round-3/\",\"urlToImage\":\"https://sportshub.cbsistatic.com/i/r/2020/06/26/73828f4d-7010-45ed-b1a6-9b879a7bce8a/thumbnail/1200x675/3861d32241531134a6bfc0cce235636b/rory-mcilroy-2020-travelers.png\"},{\"content\":\"In this Saturday, June 13, 2020 file photo, people enjoy the warm weather on the beach in Barcelona, Spain. European Union envoys are close to finalizing a list of countries whose citizens will be al… [+3744 chars]\",\"description\":\"Americans are almost certain to be excluded in the short term due to the number of U.S. coronavirus cases.\",\"publishedAt\":\"2020-06-27T13:40:42Z\",\"source\":{\"id\":\"usa-today\",\"name\":\"USA Today\"},\"title\":\"European Union narrows down border list, United States unlikely to make the cut - USA TODAY\",\"url\":\"https://www.usatoday.com/story/travel/news/2020/06/27/eu-narrows-down-border-list-us-unlikely-make-cut/3269592001/\",\"urlToImage\":\"https://www.gannett-cdn.com/presto/2020/06/27/USAT/696f3e59-14e6-40bd-805b-1bdec31411d4-AP_Virus_Outbreak_Europe_Borders.jpg?crop\\u003d4999,2812,x0,y333\\u0026width\\u003d3200\\u0026height\\u003d1680\\u0026fit\\u003dbounds\"},{\"author\":\"CNN Wire\",\"content\":\"At least five states reported their highest single day record of COVID-19 cases Friday, adding to the growing concern of case increases that has sent many states backpedaling on their reopening plans… [+6188 chars]\",\"description\":\"At least five states reported their highest single day record of COVID-19 cases Friday, adding to the growing concern of case increases that has sent many states backpedaling on their reopening plans. Florida, Georgia, Idaho, Tennessee and Utah all reported r…\",\"publishedAt\":\"2020-06-27T13:04:31Z\",\"source\":{\"name\":\"WGN TV Chicago\"},\"title\":\"5 states reported their highest number of coronavirus cases in one day - WGN TV Chicago\",\"url\":\"https://wgntv.com/news/coronavirus/5-states-reported-their-highest-number-of-coronavirus-cases-in-one-day/\",\"urlToImage\":\"https://wgntv.com/wp-content/uploads/sites/5/2020/06/GettyImages-1223045718.jpg?w\\u003d1280\\u0026h\\u003d720\\u0026crop\\u003d1\"},{\"author\":\"Agence France-Presse\",\"content\":\"LOréal al has announced it will remove words like whitening from its products, as global anti-racism protests continue.\\r\\nThe LOréal Group has decided to remove the words white/whitening, fair/fairnes… [+1115 chars]\",\"description\":\"Announcement comes against backdrop of global anti-racism protests\",\"publishedAt\":\"2020-06-27T12:51:28Z\",\"source\":{\"name\":\"The Guardian\"},\"title\":\"L\\u0027Oréal to remove words like \\u0027whitening\\u0027 from skincare products - The Guardian\",\"url\":\"https://www.theguardian.com/world/2020/jun/27/loreal-to-remove-words-like-whitening-from-skincare-products\",\"urlToImage\":\"https://i.guim.co.uk/img/media/70906353fad195d791239de3d53139d11dd159b7/0_314_4715_2829/master/4715.jpg?width\\u003d1200\\u0026height\\u003d630\\u0026quality\\u003d85\\u0026auto\\u003dformat\\u0026fit\\u003dcrop\\u0026overlay-align\\u003dbottom%2Cleft\\u0026overlay-width\\u003d100p\\u0026overlay-base64\\u003dL2ltZy9zdGF0aWMvb3ZlcmxheXMvdGctZGVmYXVsdC5wbmc\\u0026enable\\u003dupscale\\u0026s\\u003d6704074bf6bd1a20de1f30ac117e2254\"},{\"author\":\"Zecharias Zelalem\",\"content\":\"In an extension of a bilateral dispute between Ethiopia and Egypt over the \$4.8 billion Grand Ethiopian Renaissance Dam being built on the Nile River, Egyptian hackers launched a cyber attack on a nu… [+2832 chars]\",\"description\":\"The animosity has seen Ethiopia enter the crosshairs of Egyptian hackers numerous times in recent years.\",\"publishedAt\":\"2020-06-27T12:45:30Z\",\"source\":{\"name\":\"Quartz India\"},\"title\":\"Egypt cyber attack on Ethiopia is strike over the Grand Dam - Quartz Africa\",\"url\":\"https://qz.com/africa/1874343/egypt-cyber-attack-on-ethiopia-is-strike-over-the-grand-dam/\",\"urlToImage\":\"https://cms.qz.com/wp-content/uploads/2020/06/RTS3G6BI-e1593257023699.jpg?quality\\u003d75\\u0026strip\\u003dall\\u0026w\\u003d1400\"},{\"author\":\"NBA Insiders\",\"content\":\"After Friday\\u0027s release of the new 2020 NBA schedule, it\\u0027s time to look ahead at the games, players and teams that will be most compelling when the season resumes on July 30 at the ESPN Wide World of … [+10342 chars]\",\"description\":\"Our analysts break down the biggest games, the races to watch and what they expect in the NBA postseason.\",\"publishedAt\":\"2020-06-27T12:39:27Z\",\"source\":{\"name\":\"ESPN\"},\"title\":\"2020 NBA schedule debate - Best games, playoff races and NBA title favorites - ESPN\",\"url\":\"https://www.espn.com/nba/story/_/id/29370346/best-games-playoff-races-nba-title-favorites\",\"urlToImage\":\"https://a4.espncdn.com/combiner/i?img\\u003d%2Fphoto%2F2020%2F0626%2Fr712932_1296x729_16%2D9.jpg\"},{\"author\":\"Jordan Valinsky, CNN Business\",\"description\":\"Retailers including JCPenney, GNC, and Signet Jewelers are closing thousands of locations in June as shoppers shift their behaviors and the coronavirus pandemic continues to cripple their bottom line.\",\"publishedAt\":\"2020-06-27T12:24:54Z\",\"source\":{\"id\":\"cnn\",\"name\":\"CNN\"},\"title\":\"These mall staples announced thousands of store closures in June - CNN\",\"url\":\"https://www.cnn.com/2020/06/27/business/stores-closing-coronavirus-june/index.html\",\"urlToImage\":\"https://cdn.cnn.com/cnnnext/dam/assets/200625142213-zara-shopper-us-file-restricted-super-tease.jpg\"},{\"author\":\"https://www.facebook.com/bbcnews\",\"content\":\"Image caption\\r\\n PC David Whyte is being treated in hospital for serious injuries\\r\\nA police officer who was critically injured in a mass stabbing in Glasgow is now in a stable condition in hospital, p… [+4139 chars]\",\"description\":\"PC David Whyte is among six people injured in a mass stabbing in Glasgow on Friday.\",\"publishedAt\":\"2020-06-27T12:02:10Z\",\"source\":{\"name\":\"BBC News\"},\"title\":\"Glasgow hotel stabbings PC David Whyte \\u0027stable\\u0027 - BBC 中文网\",\"url\":\"https://www.bbc.com/news/uk-scotland-glasgow-west-53204696\",\"urlToImage\":\"https://ichef.bbci.co.uk/news/1024/branded_news/14318/production/_113121728_33cdc9a3-1a2d-4cb5-b3bf-8a04aaa81c6a.jpg\"},{\"author\":\"MPR News Staff\",\"content\":\"The number of confirmed COVID-19 cases in Minnesota climbed past 35,000 on Saturday, as state health officials expressed concerns that younger adults arent doing enough to prevent the virus spread.\\r\\n… [+9458 chars]\",\"description\":\"The number of confirmed COVID-19 cases in Minnesota climbed past 35,000 on Saturday, as state health officials expressed concerns that younger adults aren’t doing enough to prevent the virus’ spread.\",\"publishedAt\":\"2020-06-27T12:00:00Z\",\"source\":{\"name\":\"Minnesota Public Radio News\"},\"title\":\"Latest on COVID-19 in MN: Majority of cases now in young people - Minnesota Public Radio News\",\"url\":\"https://www.mprnews.org/story/2020/06/27/latest-on-covid19-in-mn\",\"urlToImage\":\"https://img.apmcdn.org/d90dcbbdb5b3b6027ee31ef63b0abbc6837d48b1/widescreen/afd85b-20200423-goldy-coronavirus.jpg\"},{\"description\":\"President Trump signed an executive order Friday aimed at protecting U.S. monuments after some were brought down and others are caught in a fierce debate ove...\",\"publishedAt\":\"2020-06-27T11:51:54Z\",\"source\":{\"name\":\"YouTube\"},\"title\":\"Trump signs executive order enacting harsh penalties for vandalizing monuments - CBS This Morning\",\"url\":\"https://www.youtube.com/watch?v\\u003dNsP696tbnq0\",\"urlToImage\":\"https://i.ytimg.com/vi/NsP696tbnq0/maxresdefault.jpg\"}],\"status\":\"ok\",\"totalResults\":38}"
    var gson = Gson()
    val token = object : TypeToken<NewsResponse>() {}.type
    val newsResponse = gson.fromJson<NewsResponse>(str, token)


    @Mock
    private lateinit var repo: NewsRepository

    @Mock
    private lateinit var service: NewsService

    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Spy
    private val teamListLiveData: MutableLiveData<Output<NewsResponse>> = MutableLiveData()

    @Mock
    lateinit var observer: Observer<Output<NewsResponse>>

    @Before
    fun setUp() {
        repo = NewsRepository(service);
        viewModel = NewListViewModel(repo)

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        assertTrue(true)
    }

    @Test
    fun loadArticleListTest() {
        Output.loading<NewsResponse>(true)
        `when`(repo.getTopHeadlines(COUNTRY, API_KEY)).thenReturn(Single.just(newsResponse))
        viewModel.fetchNewList(COUNTRY, API_KEY).observeForever(observer)
        Output.loading<NewsResponse>(false)
        verify(observer).onChanged(Output.success(newsResponse))
    }


    @Test
    fun errorArticleListTest() {
        Output.loading<NewsResponse>(true)
        val throwable = Throwable()
        `when`(repo.getTopHeadlines(COUNTRY, API_KEY)).thenReturn(Single.error(throwable))
        viewModel.fetchNewList(COUNTRY, API_KEY).observeForever(observer)
      //  verify(observer).onChanged(Output.failure(Errors("230","")))
    }
}