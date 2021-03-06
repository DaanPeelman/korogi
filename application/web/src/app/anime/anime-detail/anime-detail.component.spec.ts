import { ComponentFixture, fakeAsync, inject, TestBed, tick, waitForAsync } from "@angular/core/testing";
import { AnimeDetailComponent } from "./anime-detail.component";
import { AnimeService } from "../../shared/services/anime/anime.service";
import { anyString, instance, mock, verify, when } from "ts-mockito";
import { CUSTOM_ELEMENTS_SCHEMA } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { of as observableOf } from "rxjs";
import { EnrichedResource } from "../../shared/resources/final/enriched-resource";
import { AnimeTestData } from "../../testing/test-data/anime-test-data";
import { StubUtil } from "../../testing/util/stub-util";
import { EpisodeTestData } from "../../testing/test-data/episode-test-data";
import { PersonageTestData } from "../../testing/test-data/personage-test-data";
import { BaseAnimeRelation } from "../../generated/models";
import { AnimeDTO } from "../../shared/models/anime-dto";
import { ParamMapImpl } from "../../testing/util/param-map-impl";

describe("AnimeDetailComponent", () => {
    let component: AnimeDetailComponent;
    let fixture: ComponentFixture<AnimeDetailComponent>;

    const animeServiceMock: AnimeService = mock(AnimeService);

    let activatedRouteStub;

    beforeEach(waitForAsync(() => {
        TestBed.configureTestingModule(
            {
                schemas: [
                    CUSTOM_ELEMENTS_SCHEMA
                ],
                imports: [],
                declarations: [
                    AnimeDetailComponent
                ],
                providers: [
                    {
                        provide: AnimeService,
                        useFactory: () => instance(animeServiceMock)
                    },
                    {
                        provide: ActivatedRoute,
                        useFactory: () => StubUtil.subActivatedRoute()
                    }
                ]
            }
        )
               .compileComponents();
    }));

    beforeEach(inject([ActivatedRoute], (_activatedRouteStub: ActivatedRoute) => {
        activatedRouteStub = _activatedRouteStub;
    }));

    beforeEach(() => {
        fixture = TestBed.createComponent(AnimeDetailComponent);
        component = fixture.componentInstance;
    });

    describe("ngOnInit", () => {
        it(
            "should call the AnimeService to load the Anime with the identifier retrieved from the path and set all the fields on the component",
            fakeAsync(() => {
                const id: string = "1";
                const params = new ParamMapImpl();
                params.add("id", id);

                const embedded = [];
                embedded[BaseAnimeRelation.PREQUAL] = AnimeTestData.naruto();
                embedded[BaseAnimeRelation.SEQUAL] = AnimeTestData.steinsGateEgoisticPoriomania();
                embedded[BaseAnimeRelation.EPISODES] = [
                    EpisodeTestData.steinsGate_episode1(),
                    EpisodeTestData.steinsGate_episode2()
                ];
                embedded[BaseAnimeRelation.PERSONAGES] = [
                    PersonageTestData.okabeRintarou(),
                    PersonageTestData.makiseKurisu()
                ];

                const enrichedResource: EnrichedResource<AnimeDTO> = new EnrichedResource<AnimeDTO>(
                    AnimeTestData.steinsGate(),
                    []
                );
                enrichedResource.embedded = embedded;

                when(animeServiceMock.findAnime(
                    id,
                    BaseAnimeRelation.PREQUAL,
                    BaseAnimeRelation.SEQUAL,
                    BaseAnimeRelation.EPISODES,
                    BaseAnimeRelation.PERSONAGES
                )).thenReturn(observableOf(enrichedResource));

                component.ngOnInit();

                activatedRouteStub.paramMap.next(params);

                tick();

                expect(component.anime).toEqual(AnimeTestData.steinsGate());
                expect(component.prequal).toEqual(AnimeTestData.naruto());
                expect(component.sequal).toEqual(AnimeTestData.steinsGateEgoisticPoriomania());
                expect(component.episodes.length).toEqual(2);
                expect(component.episodes).toContain(EpisodeTestData.steinsGate_episode1());
                expect(component.episodes).toContain(EpisodeTestData.steinsGate_episode2());
                expect(component.personages.length).toEqual(2);
                expect(component.personages).toContain(PersonageTestData.okabeRintarou());
                expect(component.personages).toContain(PersonageTestData.makiseKurisu());
            })
        );

        it(
            "should call the AnimeService to load the Anime with the full identifier retrieved from the path when the identifier is larger than 1 in length",
            fakeAsync(() => {
                const id: string = "12"; // id is 2 in length
                const params = new ParamMapImpl();
                params.add("id", id);

                const embedded = [];
                embedded[BaseAnimeRelation.PREQUAL] = AnimeTestData.naruto();

                const enrichedResource: EnrichedResource<AnimeDTO> = new EnrichedResource<AnimeDTO>(
                    AnimeTestData.steinsGate(),
                    []
                );
                enrichedResource.embedded = embedded;

                when(animeServiceMock.findAnime(anyString(), anyString(), anyString(), anyString(), anyString()))
                    .thenReturn(observableOf(enrichedResource));

                component.ngOnInit();

                activatedRouteStub.paramMap.next(params);

                tick();

                expect(component.anime).toEqual(AnimeTestData.steinsGate());

                // verify findAnime was called with the full id and only once
                verify(animeServiceMock.findAnime(
                    id,
                    BaseAnimeRelation.PREQUAL,
                    BaseAnimeRelation.SEQUAL,
                    BaseAnimeRelation.EPISODES,
                    BaseAnimeRelation.PERSONAGES
                )).once();
            })
        );
    });
});
