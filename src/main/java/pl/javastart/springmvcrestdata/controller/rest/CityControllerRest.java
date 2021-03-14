package pl.javastart.springmvcrestdata.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.javastart.springmvcrestdata.model.City;
import pl.javastart.springmvcrestdata.repository.CityRepository;

import java.util.Comparator;
import java.util.List;

@Controller
@RestController("/api/cities")
public class CityControllerRest {
    private CityRepository cityRepository;

    @Autowired
    public CityControllerRest(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<City> getCities(@RequestParam(defaultValue = "name") String orderBy){
        List<City> cityRepositoryAll = cityRepository.findAll();
        if ("name".equals(orderBy)){
            cityRepositoryAll.sort(Comparator.comparing(City::getName));
        } else if ("population".equals(orderBy)){
            cityRepositoryAll.sort(Comparator.comparing(City::getPopulation));
        }
        return cityRepositoryAll;
    }

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<City> getCity(@PathVariable Long id){
        if (id < 0 || id >= cityRepository.count()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(cityRepository.findById(id).get());
        }
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody City city){
        cityRepository.save(city);
    }
}
