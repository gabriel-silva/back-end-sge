package com.backend.sge.resource;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.sge.model.MeasurementUnit;
import com.backend.sge.repository.MeasurementUnitRepository;
import com.backend.sge.validation.MeasurementUnitValidation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MeasurementUnitResource.class})
public class MeasurementUnitResourceTest {

    private ObjectMapper objectMapper;

    @Autowired
    private MeasurementUnitResource measurementUnitResource;

    private MockMvc mockMvc;

    @MockBean
    private MeasurementUnitRepository measurementUnitRepository;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(measurementUnitResource).build();
    }

    @Test
    public void createMeasurementUnit() throws Exception {

        MeasurementUnitValidation measurementUnitValidation = new MeasurementUnitValidation();
        measurementUnitValidation.setName("UND");

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setName(measurementUnitValidation.getName());

        when(measurementUnitRepository.save(any(MeasurementUnit.class))).thenReturn(measurementUnit);

        mockMvc.perform(post("/api/measurementUnit")
                .content(objectMapper.writeValueAsString(measurementUnitValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("UND")));

        verify(measurementUnitRepository).save(any(MeasurementUnit.class));

    }

    @Test
    public void updateMeasurementUnit() throws Exception {

        MeasurementUnitValidation measurementUnitValidation = new MeasurementUnitValidation();
        measurementUnitValidation.setName("UND");

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setName(measurementUnitValidation.getName());

        when(measurementUnitRepository.findById((long) 1)).thenReturn(Optional.of(measurementUnit));
        when(measurementUnitRepository.save(any(MeasurementUnit.class))).thenReturn(measurementUnit);

        mockMvc.perform(put("/api/measurementUnit/1")
                .content(objectMapper.writeValueAsString(measurementUnitValidation))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("UND")));

    }

    @Test
    public void getMeasurementUnitById() throws Exception {

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setId((long) 1);
        measurementUnit.setName("UND");

        when(measurementUnitRepository.findById((long) 1)).thenReturn(Optional.of(measurementUnit));

        mockMvc.perform(get("/api/measurementUnit/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("UND")));

        verify(measurementUnitRepository).findById((long) 1);

    }

    @Test
    public void getMeasurementUnitByIdNotFound() throws Exception {
        mockMvc.perform(get("/api/measurementUnit/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteMeasurementUnit() throws Exception {

        MeasurementUnit measurementUnit = new MeasurementUnit();
        measurementUnit.setId((long) 1);
        measurementUnit.setName("UND");

        when(measurementUnitRepository.findById((long) 1)).thenReturn(Optional.of(measurementUnit));

        mockMvc.perform(delete("/api/measurementUnit/1"))
                .andExpect(status().isNoContent());

        verify(measurementUnitRepository, times(1)).delete(measurementUnit);

    }

}