package com.freightboard.loads;


/**
 * The loads API. Put @RequestMapping("/api/loads") on the class, so each method only adds the rest of the path.
 *
 * TODO 3: GET  /api/loads                -> 200 + list. Optional query params ?status=OPEN and ?origin=LS1
 *         GET  /api/loads/{id}           -> 200 + load, or 404
 * TODO 4: POST /api/loads                -> 201 Created + the new load as the body
 *                                           + a Location header with the new load's URL (http://.../api/loads/7)
 * TODO 5: PUT  /api/loads/{id}           -> 200 + updated load, or 404
 *         POST /api/loads/{id}/cancel    -> 200 + cancelled load, or 404
 * TODO 6: DELETE /api/loads/{id}         -> 204 No Content, or 404
 */
public class LoadController {

    private final LoadService loadService;

    public LoadController(LoadService loadService) {
        this.loadService = loadService;
    }
}
