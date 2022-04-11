package com.easymind.ui;

import com.easymind.beans.IdeaNode;
import com.easymind.beans.MindMap;
import javafx.scene.Group;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayUtil {

    public enum ALIGNMENT{
        CENTER,
        LEFT_TO_RIGHT,
        RIGHT_TO_LEFT
    }

    private static final double DEFAULT_VSPACE = 30.0;
    private static final double DEFAULT_HSPACE = 50.0;

    private static double VSpace = DEFAULT_VSPACE;
    private static double HSpace = DEFAULT_HSPACE;
    private static MindMap mindMap;

    public static void init(MindMap mindMap){
        DisplayUtil.mindMap=mindMap;
    }

    public static Group getMindMapView(){
        IdeaNode centralIdea = mindMap.getCentralIdea();
        Map<IdeaNode,Group> ideaNodeNodeMap = new HashMap<>();

        for(IdeaNode ideaNode:centralIdea.getLeaves()){
            Group group = new Group(ideaNode.getTextField());
            ideaNodeNodeMap.put(ideaNode,group);
        }

        int currentDepth = centralIdea.getDeepestDepth();
        while(currentDepth != 1){
            currentDepth--;

            for (IdeaNode parent: centralIdea.getNodesInSpecificDepth(currentDepth)) {
                if(!parent.isLeaf()){
                    List<Group> groups = new ArrayList<>();
                    for(IdeaNode node : parent.getChildIdea()){
                        groups.add(ideaNodeNodeMap.get(node));
                    }
                    ideaNodeNodeMap.put(parent,createGroup(parent.getTextField(),groups));
                }
            }
        }
        return ideaNodeNodeMap.get(centralIdea);
    }

    public static Group createGroup(TextField parent, List<Group> groups){
        Group group = new Group();
        double yOffset = 0;
        group.getChildren().addAll(groups);
        group.getChildren().add(parent);
        for(Group node : groups){
            node.setLayoutX(parent.getMinWidth() + HSpace);
            node.setLayoutY(yOffset);
            yOffset = yOffset +node.getLayoutBounds().getHeight() + VSpace;
        }
        parent.setLayoutX(0);
        parent.setLayoutY(yOffset / 2 - parent.getHeight());
        return group;
    }

    public static double getVSpace() { return VSpace;}
    public static double getHSpace() { return HSpace;}

    public static void setHSpace(double HSpace) { DisplayUtil.HSpace = HSpace;}
    public static void setVSpace(double VSpace) { DisplayUtil.VSpace = VSpace;}

}
