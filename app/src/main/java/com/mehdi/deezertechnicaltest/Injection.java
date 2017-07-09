package com.mehdi.deezertechnicaltest;

import com.mehdi.deezertechnicaltest.data.executor.PriorityThreadPoolExecutor;
import com.mehdi.deezertechnicaltest.data.home.HomeDataRepository;
import com.mehdi.deezertechnicaltest.data.home.parsing.HomeStreamParser;
import com.mehdi.deezertechnicaltest.data.home.repository.HomeRepository;
import com.mehdi.deezertechnicaltest.data.home.service.HomeService;
import com.mehdi.deezertechnicaltest.data.home.service.HomeServiceImpl;

/**
 * Class that will provide dependencies.
 */
public class Injection {

  private static Injection instance;

  private PriorityThreadPoolExecutor backgroundExecutor;
  private HomeService homeService;

  private HomeRepository homeRepository;

  public static Injection getInstance() {
    if (instance == null) {
      throw new RuntimeException("init() should be call first");
    }
    return instance;
  }

  static void init() {
    instance = new Injection();
  }

  private Injection() {
    backgroundExecutor = new PriorityThreadPoolExecutor();
    homeService = new HomeServiceImpl(new HomeStreamParser());
  }

  public HomeRepository getHomeRepository() {
    if (homeRepository == null) {
      homeRepository = new HomeDataRepository(backgroundExecutor, homeService);
    }
    return homeRepository;
  }

  public PriorityThreadPoolExecutor getBackgroundExecutor() {
    return backgroundExecutor;
  }
}
