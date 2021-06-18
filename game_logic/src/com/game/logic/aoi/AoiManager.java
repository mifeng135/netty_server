package com.game.logic.aoi;

import com.game.logic.model.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.game.logic.Constants.GRID_HEIGHT;
import static com.game.logic.Constants.GRID_WIDTH;


// coordinate
//   100   100   100   100
// |-----------------------|
// |  0  |  1  |  2  |  3  | 100
// |-----O-----------------|
// |  4  |  5  |  6  |  7  | 100
// |-----+-----------------|
// |  8  |  9  |  10 |  11 | 100
// |-----------------------|
public class AoiManager {

    private float gridWidth;
    private float gridHeight;
    private int gridCntX;
    private int gridCntY;
    private Map<Integer, Grid> gridMap = new HashMap<>();

    public AoiManager(float gridWidth, float gridHeight, int sceneWidth, int sceneHeight) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        gridCntX = (int) Math.ceil(sceneWidth / GRID_WIDTH);
        gridCntY = (int) Math.ceil(sceneHeight / GRID_HEIGHT);
        initGrid();
    }


    /**
     * 初始化地图格子
     */
    private void initGrid() {
        for (int y = 0; y < gridCntY; y++) {
            for (int x = 0; x < gridCntX; x++) {
                int id = y * gridCntX + x;
                Grid grid = new Grid(id, gridWidth * x,gridHeight * y);
                gridMap.put(id, grid);
            }
        }
    }

    /**
     * 通过id获取周围都格子
     *
     * @param id
     * @return
     */
    public List<Grid> getSurroundGridsByGid(int id) {
        Grid grid = gridMap.get(id);
        List<Grid> gridList = new ArrayList<>();
        gridList.add(grid);

        int idx = id % gridCntX;

        //获得左边是否有格子
        if (idx > 0) {
            gridList.add(gridMap.get(id - 1));
        }
        //获得右边是否有格子
        if (idx < gridCntX - 1) {
            gridList.add(gridMap.get(id + 1));
        }
        //寻找周围的格子
        List<Grid> otherGrid = new ArrayList<>();
        for (int i = 0; i < gridList.size(); i++) {
            int gridId = gridList.get(i).getId();
            int idy = gridId / gridCntY;
            if (idy > 0) {
                otherGrid.add(gridMap.get(idy - gridCntX));
            }
            if (idy < gridCntY - 1) {
                otherGrid.add(gridMap.get(idy + gridCntX));
            }
        }
        gridList.addAll(otherGrid);
        return gridList;
    }

    public List<Player> playerList(int gridId) {
        List<Grid> gridList = getSurroundGridsByGid(gridId);
        List<Player> playerList = new ArrayList<>();
        for (Grid grid : gridList) {
            playerList.addAll(grid.getPlayerList());
        }
        return playerList;
    }

    public Grid getGrid(int gridId) {
        return gridMap.get(gridId);
    }

    public int getGridCntX() {
        return gridCntX;
    }

    public int getGridCntY() {
        return gridCntY;
    }
}
