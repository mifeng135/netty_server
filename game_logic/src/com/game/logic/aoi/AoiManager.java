package com.game.logic.aoi;

import com.game.logic.model.Player;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

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
    private int gridCntX; //列数
    private int gridCntY; //行数
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
                Grid grid = new Grid(id, gridWidth * x, gridHeight * y);
                gridMap.put(id, grid);
            }
        }
        for (Map.Entry<Integer, Grid> m : gridMap.entrySet()) {
            List<Grid> gridList = getSurroundGridsByGid(m.getKey());
            m.getValue().setSurroundList(gridList);
        }
    }

    /**
     * 通过id获取周围都格子
     *
     * @param id
     * @return
     */
    private List<Grid> getSurroundGridsByGid(int id) {
        Grid grid = gridMap.get(id);
        List<Grid> gridList = new ArrayList<>();
        gridList.add(grid);

        int column = getColumnByGridId(id); //列数

        //获得左边是否有格子
        if (column > 0) {
            gridList.add(gridMap.get(id - 1));
        }
        //获得右边是否有格子
        if (column < gridCntX - 1) {
            gridList.add(gridMap.get(id + 1));
        }
        //寻找周围的格子
        List<Grid> otherGrid = new ArrayList<>();
        for (int i = 0; i < gridList.size(); i++) {
            int gridId = gridList.get(i).getId();
            int row = getRowByGridId(gridId); //行数
            if (row > 0) {
                otherGrid.add(gridMap.get(gridId - gridCntX));
            }
            if (row < gridCntY - 1) {
                otherGrid.add(gridMap.get(gridId + gridCntX));
            }
        }
        gridList.addAll(otherGrid);
        return gridList;
    }

    /**
     * 获取以gridId为中心的所有player
     *
     * @param gridId
     * @return
     */
    public List<Player> getPlayerList(int gridId) {
        List<Grid> gridList = getGrid(gridId).getSurroundList();
        List<Player> playerList = new ArrayList<>();
        for (Grid grid : gridList) {
            playerList.addAll(grid.getPlayerList());
        }
        return playerList;
    }


    /**
     * 获取以gridId为中心的所有playerIndex
     * @param gridId
     * @return
     */
    public List<Integer> getPlayerIndexList(int gridId) {
        List<Grid> gridList = getGrid(gridId).getSurroundList();
        List<Integer> playerIndexList = new ArrayList<>();
        for (Grid grid : gridList) {
            playerIndexList.addAll(grid.getPlayerIndexList());
        }
        return playerIndexList;
    }

    /**
     * 获取以gridId为中心的周围gridList
     *
     * @param gridId
     * @return
     */
    public List<Grid> getGridList(int gridId) {
        return getGrid(gridId).getSurroundList();
    }

    /**
     * 获取grid
     *
     * @param gridId
     * @return
     */
    public Grid getGrid(int gridId) {
        return gridMap.get(gridId);
    }

    /**
     * 获得列数
     *
     * @return
     */
    public int getGridCntX() {
        return gridCntX;
    }

    /**
     * 获得行数
     *
     * @return
     */
    public int getGridCntY() {
        return gridCntY;
    }

    /**
     * 获取所在行数[0, gridCntX - 1]
     *
     * @param gridId
     * @return
     */
    public int getRowByGridId(int gridId) {
        return gridId / gridCntX;
    }

    /**
     * 获取所在列数[0, gridCntX - 1]
     *
     * @param gridId
     * @return
     */
    public int getColumnByGridId(int gridId) {
        return gridId % gridCntX;
    }
}
