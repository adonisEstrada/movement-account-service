package com.adonis.nttdata.service;

import com.adonis.nttdata.entity.Movement;
import com.adonis.nttdata.presentation.presenter.MovementPresenter;
import com.adonis.nttdata.presentation.presenter.MovementsReportPresenter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface MovementService {
    Movement getMovementById(UUID movementId);

    MovementPresenter saveUpdateMovement(MovementPresenter movementPresenter);

    void deleteMovementById(UUID movementId);

    List<MovementsReportPresenter> getMovementByClientAndDates(String dni, Date initDate, Date endDate);

    MovementPresenter toPresenter(Movement movement);
}
