package com.backend.sge.resource;

import com.backend.sge.exception.NotFoundException;
import com.backend.sge.model.MeasurementUnit;
import com.backend.sge.repository.MeasurementUnitRepository;
import com.backend.sge.validation.MeasurementUnitValidation;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class MeasurementUnitResource {

    @Autowired
    private MeasurementUnitRepository measurementUnitRepository;

    @ApiOperation(value = "Cadastrar unidade de medida")
    @RequestMapping(value = "/measurementUnit", method = RequestMethod.POST)
    public ResponseEntity<MeasurementUnit> createMeasurementUnit(@Valid @RequestBody MeasurementUnitValidation measurementUnitValidation) {
        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setName(measurementUnitValidation.getName());
        MeasurementUnit responseMeasurementUnit = measurementUnitRepository.save(measurementUnit);
        return new ResponseEntity<MeasurementUnit>(responseMeasurementUnit, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Atualizar unidade de medida")
    @RequestMapping(value = "/measurementUnit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MeasurementUnit> updateMeasurementUnit(@PathVariable(value = "id") long id,
                                                                  @Valid @RequestBody MeasurementUnitValidation measurementUnitValidation) throws NotFoundException {
        MeasurementUnit measurementUnit = measurementUnitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidade de medida não encontrada com o id :: " + id));
        measurementUnit.setId(id);
        measurementUnit.setName(measurementUnitValidation.getName());
        MeasurementUnit responseMeasurementUnit = measurementUnitRepository.save(measurementUnit);
        return new ResponseEntity<MeasurementUnit>(responseMeasurementUnit, HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Deletar unidade de medida")
    @RequestMapping(value = "/measurementUnit/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<MeasurementUnit> deleteMeasurementUnit(@PathVariable(name = "id") long id) throws NotFoundException {
        MeasurementUnit measurementUnit = measurementUnitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidade de medida não encontrada com o id :: " + id));
        measurementUnitRepository.delete(measurementUnit);
        return new ResponseEntity<MeasurementUnit>(HttpStatus.NO_CONTENT);
    }

    @ApiOperation(value = "Listar unidade de medida pelo id")
    @RequestMapping(value = "/measurementUnit/{id}", method = RequestMethod.GET)
    public ResponseEntity<MeasurementUnit> getMeasurementUnitById(@PathVariable(name = "id") long id) throws NotFoundException {
        MeasurementUnit measurementUnit = measurementUnitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidade de medida não encontrada com o id :: " + id));
        return new ResponseEntity<MeasurementUnit>(measurementUnit, HttpStatus.OK);
    }

    @ApiOperation(value = "Listar unidade de medidas")
    @RequestMapping(value = "/measurementUnit", method = RequestMethod.GET)
    public Page<MeasurementUnit> getAllMeasurementsUnits(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        return measurementUnitRepository.findAll(PageRequest.of(offset, limit));
    }

}
