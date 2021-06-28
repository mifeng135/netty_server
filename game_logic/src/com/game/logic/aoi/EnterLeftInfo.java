package com.game.logic.aoi;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class EnterLeftInfo {
    private List<Grid> enterList;
    private List<Grid> leftList;
}
