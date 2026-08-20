package mvc.view.api;

import mvc.model.api.SliceableModel;

public interface GameAreaPanel {

    public void addSliceables(SliceableModel sliceable);

    public int getWidth();

    public int getHeight();
    
}