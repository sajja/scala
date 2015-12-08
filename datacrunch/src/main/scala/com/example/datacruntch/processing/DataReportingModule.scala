package com.example.datacruntch.processing

import java.util.Date

/**
  * Created by sajith on 11/16/15.
  */
trait EventDataReportingModule extends DomainModelModule {
  def load(from: Date, to: Date): List[Event]
}
