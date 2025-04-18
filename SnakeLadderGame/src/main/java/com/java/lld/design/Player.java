package com.java.lld.design;

import java.util.UUID;

public class Player {

  private String name;
  private String id;

  public Player(String name) {
    this.name = name;
    this.id = UUID.randomUUID().toString();
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

}
