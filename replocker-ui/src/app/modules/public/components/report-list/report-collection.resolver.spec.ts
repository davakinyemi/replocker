import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';

import { reportCollectionResolver } from './report-collection.resolver';

describe('reportCollectionResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
      TestBed.runInInjectionContext(() => reportCollectionResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
