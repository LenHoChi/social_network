package com.example.utils.convert;

import com.example.dto.RelationshipDTO;
import com.example.model.Relationship;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RelationshipConvert {
    static ModelMapper modelMapper = new ModelMapper();
    public static RelationshipDTO modelToDTO(Relationship relationship){
        RelationshipDTO relationshipDTO = modelMapper.map(relationship, RelationshipDTO.class);
        return relationshipDTO;
    }
    public static List<RelationshipDTO> listModelToListDTO(List<Relationship> list){
        List<RelationshipDTO> list1 = new ArrayList<>();
        return list.stream().map(ele -> modelMapper.map(ele, RelationshipDTO.class)).collect(Collectors.toList());
//        for(int i=0;i<list.size();i++){
//            RelationshipDTO relationshipDTO = modelMapper.map(list.get(i), RelationshipDTO.class);
//            list1.add(relationshipDTO);
//        }
//        return list1;
    }
}
